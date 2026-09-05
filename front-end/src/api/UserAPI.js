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
export async function fetchOnePageOfUsers(cursor) {
    const response = await axios.get("http://localhost:8080/api/users/paging", {
        params: {
            cursor: cursor,
            size: 2
        },
        headers: {
            Authorization: `Bearer ${keycloak.token}` 
        }
    });
    return response.data;
}
export async function fetchOnePageOfProject(cursor) {
    const response = await axios.get("http://localhost:8080/api/projects/paging", {
        params: {
            page: cursor,
            size: 3
        },
        headers: {
            Authorization: `Bearer ${keycloak.token}`
        }
    });
    return response.data;
}
export async function fetchOnePageOfTask(cursor) {
    const response = await axios.get("http://localhost:8080/api/tasks/paging", {
        params: {
            page: cursor,
            size: 3
        },
        headers: {
            Authorization: `Bearer ${keycloak.token}`
        }
    });
    return response.data;
}