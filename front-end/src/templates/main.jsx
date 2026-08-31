import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import '../styles/index.css'
import MainPage from './MainPage'
import Tasks from './Tasks'
import ProjectPage from './ProjectPage'
import LoginPage from "./LoginPage"
import RegistrationPage from "./Registration"
import {useEffect} from 'react'
import keycloak from "../api/KeycloakConfiguration";

import { BrowserRouter, Routes, Route } from 'react-router';

function App() {
    useEffect(() => {
        keycloak.init({
            onLoad: "check-sso",
            pkceMethod: "S256"
        }).then(authenticated => {
            console.log("Authenticated:", authenticated);
        });
    }, []);

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