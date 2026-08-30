import axios from 'axios'

export async function getAllUsers() {
    const response = await axios.get("http://localhost:8080/api/users");
    return response.data;
}
export async function getAllProjects() {
    const response = await axios.get("http://localhost:8080/api/projects");
    return response.data;
}
export async function getAllTasks() {
    const response = await axios.get("http://localhost:8080/api/tasks")
    return response.data;
}
export async function createNewUser(newUser) {
    const response = await axios.post("http://localhost:8080/api/users", newUser)
    return response.data;
}