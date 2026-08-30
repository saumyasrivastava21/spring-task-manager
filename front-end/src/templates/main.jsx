import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import '../styles/index.css'
import MainPage from './MainPage'
import Tasks from './Tasks'
import ProjectPage from './ProjectPage'
import LoginPage from "./LoginPage"
import RegistrationPage from "./Registration"
createRoot(document.getElementById('root')).render(
  <StrictMode>
    <RegistrationPage/>
  </StrictMode>,
)
