import { useState } from 'react'

import "../styles/login.scss"
import Header from "./Header.jsx"
export default function Registration() {
    const [email, setEmail] = useState("")
    const [password, setPassword] = useState("")
    const [repeatPassword, setRepeatPassword] = useState("")


    const [isPasswordCorrect, setIsPasswordCorrect] = useState(false);

    const verifyPassword = (e) => {

        if (password === (e.target.value)) {
            setIsPasswordCorrect(true);
        }

        setRepeatPassword(e.target.value);
    }

    return (
        <>
            <Header/>
            <div className="container">
                <h1>Authorization</h1>
                <div className="login-form">
                    <form action="/" method="POST">
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
                        <input type="text" 
                                placeholder="Password:"
                                className="password-field field"
                                value={repeatPassword}
                                onChange={e => verifyPassword(e)} />
                        <input type="submit"
                                className="send-request-btn btn"
                                value="Login"
                                disabled={!isPasswordCorrect}/>
                    </form>
                </div>
            </div>



        </>
    )
}