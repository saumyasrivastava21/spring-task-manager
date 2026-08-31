import { useState, useEffect } from 'react';
import '../styles/mainPage.scss'
import Header from "./Header"
import { getAllUsers, getAllProjects, getAllTasks } from "../api/UserAPI"
import getTableNames from "../api/AvailableTablesAPI"
import DataTable from "./DataTable"
import keycloak from "../api/KeycloakConfiguration";

export default function MainPage() {
    const [entityName, setEntityName] = useState("")
    const [entities, setEntities] = useState([])
    const [dataTable, setDataTable] = useState([]);

    useEffect(() => {
        getTableNames()
            .then(data => {
                setEntities(data.data);
            })
            .catch(error => {
                console.error(error);
            });
    }, []);
    useEffect(() => {
        let loadData;

        if (entityName === "Users") {
            loadData = getAllUsers;
        } else if (entityName === "Projects") {
            loadData = getAllProjects;
        } else if (entityName === "Tasks") {
            loadData = getAllTasks;
        }

        if (!loadData) {
            setDataTable([]);
            return;
        }

        loadData()
            .then(data => {
                setDataTable(data);
            })
            .catch(error => {
                console.error(error);
            });

    }, [entityName]);

    const choseTableToLoad = (e => {
        setEntityName(e.target.value);
        
    })
    return (
        <div className="container">
            <Header />
            <div className="content">
                <div className="data-container">
                    <div className="entity-input">
                        <input type="text" 
                        name="entityName"  
                        id="" 
                        list="entities"
                        placeholder="Choose which entity need to load"
                        value={entityName}
                        onChange={e => choseTableToLoad(e)}
                          />
                        <datalist id="entities">
                            {entities.length > 0 &&
                                entities.map(value => (
                                    <option value={value} key={value} />
                                ))
                            }
                        </datalist>
                    </div>
                    <div className="entity-table-container">
                        <h1>{entityName === "" ? "Entity name" : entityName}</h1>
                        {entityName !== "" && <DataTable data={dataTable}/>}
                    </div>
                </div>
            </div>
        </div>
    );
}