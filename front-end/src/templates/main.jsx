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
            <LoginPage />
        </StrictMode>
    );
}

createRoot(document.getElementById('root')).render(
    <App />
);