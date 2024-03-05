(ns notification.system.api
  (:gen-class)
  (:require [org.httpkit.server :as server]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer :all]
            [clojure.data.json :as json]))

(def users (clojure.edn/read-string (slurp "resources/data.edn")))

(defn filter-users [users-data category]
  (try
  (->> (:users users-data) 
         (filter (fn [user]
                   (some #{category} (:categories user)))))
    (catch Exception e
      (println e))))

(defn print-and-save [text]
  (println text)
  (spit "resources/events.log" (str text "\n") :append true))


(defn send-notification [user-list category message]
  (doseq [user user-list]
    (let [username (:username user)
          phone-number (:phone_number user)
          email (:email user)
          id (:id user)
          time (.toString (java.time.LocalDateTime/now))]
      (doseq [notification (:notification user)]
        (cond
          (= (:type notification) "SMS") (print-and-save (format "%s - %s - Sending SMS to %s for user %s, message: %s" time category phone-number username message))
          (= (:type notification) "EMAIL") (print-and-save (format "%s - %s - Sending EMAIL to %s for user %s, message: %s" time category email username message))
          (= (:type notification) "PUSH") (print-and-save (format "%s - %s - Sending PUSH to %s for user %s, message: %s" time category id username message)))))))

(defn notify-request [req]
  {:status 200
   :headers {"Content-Type" "Application/json"
             "Access-Control-Allow-Origin" "*"
             "Access-Control-Allow-Credentials" "true"
             "Access-Control-Allow-Methods" "POST, GET, OPTIONS, PUT, DELETE"
             "Access-Control-Allow-Headers" "Content-Type, Accept, Authorization, Authentication, If-Match, If-None-Match, If-Modified-Since, If-Unmodified-Since"}
   :body    (->>
             (let [data (json/read-str (slurp (:body req)))
                   category (get data "category")
                   message (get data "message")
                   my-response (doall (filter-users users category))] 
               
               (try
                 (send-notification my-response category message)
                 (json/write-str {:message "Notify Success"})
                 (catch Exception e
                   (println e)))))})

(defn get-log-request [req]
  {:status 200
   :headers {"Content-Type" "text/plain" "Access-Control-Allow-Origin" "*" "Access-Control-Allow-Methods" "GET"}
   :body    (slurp "resources/events.log")})

(defn get-options [req]
  {:status 200
   :headers {"Content-Type" "application/json"
             "Access-Control-Allow-Origin" "*"
             "Access-Control-Allow-Credentials" "true"
             "Access-Control-Allow-Methods" "POST, GET, OPTIONS, PUT, DELETE"
             "Access-Control-Allow-Headers" "Content-Type, Accept"}})

(defroutes app-routes
  (POST "/v1/users/notify" [] notify-request)
  (OPTIONS "/v1/users/notify" [] get-options)
  (GET "/v1/users/log" [] get-log-request)
  (route/not-found "Error, page not found!"))


(defn -main
  "Initial function"
  [& args]
  (println "Starting application on port 8000")
  (server/run-server (wrap-defaults #'app-routes (assoc-in site-defaults [:security :anti-forgery] false)) {:port 8000}))
