import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import '../styles/index.css'
import MainPage from './MainPage'
import Tasks from './Tasks'
import ProjectPage from './ProjectPage'
import LoginPage from "./LoginPage"
import RegistrationPage from "./Registration"
import {useEffect, useState} from 'react'
import keycloak from "../api/KeycloakConfiguration";

import { BrowserRouter, Routes, Route } from 'react-router';

function App() {
    const [initialized, setInitialized] = useState(false);

    useEffect(() => {
        keycloak.init({
            onLoad: "login-required",
        }).then(authenticated => {

            setInitialized(true);
        });
    }, []);

    if (!initialized) {
        return <div>Loading...</div>;
    }

    return (
        <StrictMode>
            <BrowserRouter>
                <Routes>
                    <Route path="/login" element={<LoginPage />} />
                    <Route path="/home" element={<MainPage />} />
                </Routes>
            </BrowserRouter>
        </StrictMode>
    );
}

createRoot(document.getElementById('root')).render(
        <App />
);