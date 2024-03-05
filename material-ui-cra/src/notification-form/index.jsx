import { FormControl, InputLabel, Select, MenuItem, Box, TextField, Typography, Button } from "@mui/material";
import { useEffect, useState } from "react";
import axios from 'axios';
import LogVisualizer from "../log-visualizer";
import CustomAlert from "./CustomAlert";

const env_url = 'http://localhost:8000/'; // get from correct environment;

const NotificationForm = () => {
    const [items, setItems] = useState([]);
    const [category, setCategory] = useState('');
    const [message, setMessage] = useState('');
    const [errors, setErrors] = useState('');
    const [successMessage, setSuccessMessage] = useState('');

    useEffect(() => {
        setItems(["SPORTS", "MOVIES", "FINANCE"])
    }, []);

    const handleChange = (event) => {
        if (event.target.name === "category") {
            setCategory(event.target.value)
        } else if (event.target.name === "message") {
            setMessage(event.target.value);
        }
    };

    const handleClick = async (event) => {
        try {
            const response = await axios.post(`${env_url}v1/users/notify`, { category, message });
            console.log(response?.status);
            if (response?.status === 200) {
                setSuccessMessage('Sent correctly');
            }
        } catch (ex) {
            setErrors(ex);
        }
    }

    return (
        <div>
            <CustomAlert errors={errors} successMessage={successMessage} />
            <Box sx={{ minWidth: 120 }}>
                <Typography variant="h4">
                    Message Sender
                </Typography>
                <Typography variant="subtitle1">
                    Please enter a message and to send to the selected category.
                </Typography>
                <FormControl fullWidth>
                    <InputLabel id="select-label">Category</InputLabel>
                    <Select
                        labelId="select-label"
                        name="category"
                        value={category}
                        label="Category"
                        onChange={handleChange}>
                        {items.map((item, index) => (
                            <MenuItem name="category" key={index} value={item}>
                                {item}
                            </MenuItem>
                        ))}
                    </Select>
                </FormControl>
                <FormControl fullWidth>
                    <TextField name="message" label="Message" variant="standard" onChange={handleChange} value={message} multiline maxRows={10} rows={5} />
                </FormControl>
                <FormControl fullWidth>
                    <Button variant="contained" onClick={handleClick}>Send</Button>
                </FormControl>
                <LogVisualizer />
            </Box>
        </div>
    );
};

export default NotificationForm;