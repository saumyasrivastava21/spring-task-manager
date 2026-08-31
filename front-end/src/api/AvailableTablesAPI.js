import axios from 'axios'
import keycloak from "../api/KeycloakConfiguration";

export default async function getTableNames() {
    const data = await axios.get("http://localhost:8080/api/available-tables", {
        headers: {
            Authorization: `Bearer ${keycloak.token}`
        }
    });
    return data;
}