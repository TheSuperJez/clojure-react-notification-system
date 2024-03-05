import { Alert } from "@mui/material";
import { useEffect, useState } from "react";

const CustomAlert = ({ errors, successMessage }) => {
    const [display, setDisplay] = useState(false);
    useEffect(() => {
        setDisplay(true);
        setTimeout(() => {
            setDisplay(false);
        }, 5000);

    }, [errors, successMessage]);
    return (
        <div>{errors && display &&
            <Alert severity="error">
                {errors}
            </Alert>}
            {successMessage && display &&
                <Alert severity="success">
                    {successMessage}
                </Alert>}
        </div>)
};

export default CustomAlert;