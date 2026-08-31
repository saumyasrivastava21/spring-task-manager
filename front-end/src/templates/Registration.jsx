import { useState } from 'react'
import {createNewUser} from '../api/UserApi'
import "../styles/login.scss"
import Header from "./Header.jsx"


export default function Registration() {
    const [email, setEmail] = useState("")
    const [password, setPassword] = useState("")
    const [repeatPassword, setRepeatPassword] = useState("")
    const [position, setPosition] = useState("MANAGER")

    const [isPasswordCorrect, setIsPasswordCorrect] = useState(false);

    const verifyPassword = (e) => {

        if (password === (e.target.value)) {
            setIsPasswordCorrect(true);
        }

        setRepeatPassword(e.target.value);
    }


    const submitForm = async (e) => {
        e.preventDefault();

        const newUser = {
            email,
            password,
            position
        }

        try {
            const user = await createNewUser(newUser);
            console.log("Created user:", user);
        } catch (error) {
            console.error("Failed to create user:", error);
        }
    }
    return (
        <>
            <Header/>
            <div className="container">
                <h1>Registration</h1>
                <div className="login-form">
                    <form action="/" method="POST" onSubmit={submitForm}>
                        <input type="email" 
                                placeholder="Email:"
                                className="email-field field"
                                value={email}
                                onChange={e => setEmail(e.target.value)} />
                        <input type="password" 
                                placeholder="Password:"
                                className="password-field field"
                                value={password}
                                onChange={e => setPassword(e.target.value)} />
                        <input type="password" 
                                placeholder="Password:"
                                className="password-field field"
                                value={repeatPassword}
                                onChange={e => verifyPassword(e)} />
                        <input type="submit"
                                className="send-request-btn btn"
                                value="Registrate"
                                disabled={!isPasswordCorrect}/>
                    </form>
                </div>
            </div>



        </>
    )
}