import { useState } from 'react'

import "../styles/login.scss"
import Header from "./Header.jsx"

import keycloak from "../api/KeycloakConfiguration";

export default function LoginPage() {
    const [email, setEmail] = useState("")
    const [password, setPassword] = useState("")
    
    const handleLoginPage = () => {
        keycloak.login();
    };

    return (
        <>
            <Header />
            <div className="container">
                <button onClick={handleLoginPage}>
                    Login
                </button>
            </div>
        </>
    );
}