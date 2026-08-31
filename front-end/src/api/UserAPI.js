import axios from 'axios'
import keycloak from "../api/KeycloakConfiguration";

export async function getAllUsers() {
    const response = await axios.get("http://localhost:8080/api/users", {
        headers: {
            Authorization: `Bearer ${keycloak.token}`
        }
    });
    return response.data;
}
export async function getAllProjects() {
    const response = await axios.get("http://localhost:8080/api/projects", {
        headers: {
            Authorization: `Bearer ${keycloak.token}`
        }
    });
    return response.data;
}
export async function getAllTasks() {
    const response = await axios.get("http://localhost:8080/api/tasks", {
        headers: {
            Authorization: `Bearer ${keycloak.token}`
        }
    });
    return response.data;
}
export async function createNewUser(newUser) {
    const response = await axios.post("http://localhost:8080/api/users", newUser)
    return response.data;
}