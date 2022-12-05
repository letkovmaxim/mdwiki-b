import React, {useEffect, useState} from "react";
import List from '@mui/material/List';
import IconButton from "@mui/material/IconButton";
import "../../css/sidePanel.css";
import Divider from "@mui/material/Divider";
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import {Space} from "./Space";
import {Page} from "./Page";
import {ModalWindow} from "./Modal";
import { useParams } from "react-router-dom";

interface IComp {
    id: number,
    name:string,
    createdAt:any,
    updatedAt: any,
    shared:any
}

export const SidePanel = () => {

    const { spaceId } = useParams();

    const[list, setList] = useState<IComp[]>([])

    const[editId, setEditId] = useState<number>()

    const[styles, setStyles] = useState("addSpace")

    const[newObject, setNewObject] = useState({
        name: '',
        shared: true
    })

    const[spaceOpenId, setSpaceOpenId] = useState<number>(Number(spaceId))

    const [open, setOpen] = React.useState(false);
    const handleOpen = () => setOpen(true);
    const handleClose = () => {
        setOpen(false);
        setEditId(undefined)
        setNewObject({
            name: '',
            shared: true
        })
        setStyles("addSpace")
    }

    const [anchorEl, setAnchorEl] = React.useState<null | HTMLElement>(null);
    const openMenu = Boolean(anchorEl);
    const handleClickMenu = (event: React.MouseEvent<HTMLButtonElement>, name:string, shared:boolean, id:number) => {
        setAnchorEl(event.currentTarget);
        setEditId(id);
        setNewObject({
            name: name,
            shared: shared
        })
    };
    const handleCloseMenu = () => {
        setAnchorEl(null);
        setEditId(undefined);
        setNewObject({
            name: '',
            shared: true
        });
    };

    const handleCloseMenuForEdit = () => {
        setAnchorEl(null);
        setOpen(true);
    };

    const handleChange = (e:any) => {
        setNewObject({
            ...newObject,
            [e.target.name]: e.target.value
        });
    }

    const handleBack = () => {
        setSpaceOpenId(Number(undefined))
    }

    useEffect(() => {
        if(spaceId !== undefined){
            getNameSpace()
        }
        getList()
    }, [setList])

    async function getList() {
        let response = await fetch('/spaces?bunch=0&size=1000');
        let json = await response.json()
        setList(json)
    }

    async function getNameSpace(){
        let response = await fetch('/spaces/' + spaceId);
        let json = await response.json()
        localStorage.setItem("spaceName", json.name)
    }

    async function handleSubmit() {
        if(!errorEmpty()){
            await fetch('/spaces' + (editId ? '/' + editId : ''), {
                method: (editId) ? 'PUT' : 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(newObject),
            }).then(async response => {
                if(response.ok){
                    handleClose()
                    handleCloseMenu()
                    getList();
                    setEditId(undefined)
                    setNewObject({
                        name: '',
                        shared: true
                    })
                }else {
                    setStyles("addSpaceError")
                }
            });
        }
    }

    async function remove() {
        await fetch(`/spaces/`+ editId, {
            method: 'DELETE',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        });
        handleCloseMenu()
        await getList();
        setEditId(undefined)
        setNewObject({
            name: '',
            shared: true
        })
    }

    const errorEmpty = () => {

        let error = false;

        if(newObject.name.length === 0){
            setStyles("addSpaceError")
            error = true;
        }

        return error;
    }

    const toPage = (id:number, name:string) =>{
        setSpaceOpenId(id)
        localStorage.setItem("spaceName", name)
        localStorage.setItem("list", JSON.stringify([]))
    }

    const def = () => {}

    return(
        <div>
            {( !spaceOpenId ?
                <div>
                    <Space
                        handleOpen={handleOpen}
                        list={list}
                        anchorEl={anchorEl}
                        openMenu={openMenu}
                        handleCloseMenu={handleCloseMenu}
                        handleCloseMenuForEdit={handleCloseMenuForEdit}
                        remove={remove}
                        toPage={toPage}
                        handleClickMenu={handleClickMenu}
                    />
                </div>
            :
                <div>
                    <List>
                        <div className="headerText">
                            {localStorage.getItem("spaceName")}
                        </div>
                    </List>
                    <Divider />
                    <List>
                        <IconButton
                            sx={{
                                marginLeft: '12px'
                            }}
                            aria-label="delete"
                            size="small"
                            onClick={handleBack}
                        >
                            <ArrowBackIcon sx={{ height:25, width:25}}/>
                        </IconButton>

                        <Page idSpace={spaceOpenId}/>

                    </List>

                </div>
            )}

            <ModalWindow
                open={open}
                handleClose={handleClose}
                styles={styles}
                newObject={newObject}
                handleChange={handleChange}
                addSub={false}
                AddSubpage={def}
                handleSubmit={handleSubmit}
                editId={editId}
            />
        </div>
    )
}