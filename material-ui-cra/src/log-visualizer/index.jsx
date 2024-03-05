import { useEffect, useState } from "react";
import axios from 'axios';
import Accordion from '@mui/material/Accordion';
import AccordionSummary from '@mui/material/AccordionSummary';
import AccordionDetails from '@mui/material/AccordionDetails';
import styles from './style.css'

const env_url = 'http://localhost:8000/'; // get from correct environment;

const LogVisualizer = () => {
    const [data, setData] = useState('');
    useEffect(() => {
        try {
            const fetchData = () => {
                setInterval(async () => {
                    const response = await axios.get(`${env_url}v1/users/log`);
                    setData(response.data);
                }, 5000);

            }
            fetchData();
        } catch (ex) {
            setData('Error getting data, please try again');
        }
    }, []);
    return (<>
        <Accordion className="accordion">
            <AccordionSummary
                aria-controls="panel1-content"
                id="panel1-header"
            >
                Show Log
            </AccordionSummary>
            <AccordionDetails>
                <span className="data">{data}</span>
            </AccordionDetails>
        </Accordion>
    </>);
}

export default LogVisualizer;