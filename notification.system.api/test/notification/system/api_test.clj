(ns notification.system.api-test
  (:require [clojure.test :refer :all]
            [notification.system.api :refer :all]))

(deftest test-sms-notification
  (let [user-list [{:username "Alice" :phone_number "123-456-7890"}
                   {:username "Bob" :phone_number "987-654-3210"}]
        notification-type "SMS"
        category "MOVIES"
        mock-print-and-save (fn [message]
                              (print message)
                              (is (re-find #"SMS" message)))]
    (with-redefs [print-and-save mock-print-and-save]
      (send-notification user-list notification-type category))))


(deftest test-email-notification
  (let [user-list [{:username "Alice" :phone_number "123-456-7890"}
                   {:username "Bob" :phone_number "987-654-3210"}]
        notification-type "EMAIL"
        category "MOVIES"
        mock-print-and-save (fn [message]
                              (print message)
                              (is (re-find #"email" message)))]
    (with-redefs [print-and-save mock-print-and-save]
      (send-notification user-list notification-type category))))

