
import '../styles/header.scss'

export default function Header() {
    return (
        <header className="head-bar">
                <div id="app-name">Task manager</div>
                <div className="menu-container">
                    <ul className="menu">
                        <a href="/home">
                            <li className="menu-item">Entities</li>
                        </a>
                        <li className="menu-item">Project</li>
                    </ul>
                </div>
            </header>
    )
}