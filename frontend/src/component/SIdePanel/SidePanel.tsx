import React, {useEffect, useState} from "react";
import List from '@mui/material/List';
import IconButton from "@mui/material/IconButton";
import "../../css/sidePanel.css"
import Divider from "@mui/material/Divider";
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import {Space} from "./Space";
import {Page} from "./Page";
import {ModalWindow} from "./Modal";
import { useParams } from "react-router-dom";
import {useDispatch, useSelector} from "react-redux";
import {cleanPages, getSpace, spaceNameAndShared} from "../../redux/actions";

interface IComp {
    id: number,
    name:string,
    createdAt:any,
    updatedAt: any,
    shared:any
}

type Props = {
    checkText: string
}

export const SidePanel = ({checkText}:Props) => {

    const dispatch = useDispatch()
    const spaceList = useSelector((state:any) => state.app.spaces)
    const spaceN = useSelector((state:any) => state.app.space)
    const errorPath = useSelector((state:any) => state.app.error)
    const [notBlock, setNotBlock] = useState(true)

    const { spaceId } = useParams();

    const[editId, setEditId] = useState<number>()

    const[styles, setStyles] = useState("addSpace")

    const[error, setError] = useState("")

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
        setError("")
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
        getList()
    }, [])

    async function getList() {
        dispatch<any>(getSpace())
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
                    setError("Пространство уже существует")
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
        setNewObject({
            name: '',
            shared: true
        })
        if(spaceId === String(editId)){
            window.location.replace("/wiki");
        }
        setEditId(undefined)
    }

    const errorEmpty = () => {

        let error = false;

        if(newObject.name.length === 0){
            setStyles("addSpaceError")
            setError("Поле не должно быть пустым")
            error = true;
        }

        return error;
    }

    const toPage = (id:number, name:string, shared:boolean) =>{
        setSpaceOpenId(id)
        setNotBlock(false)
        dispatch(spaceNameAndShared(name, shared))
        dispatch(cleanPages())
    }

    const def = () => {}

    return(
        <div>
            {( !spaceOpenId || (errorPath && notBlock) ?
                    <div>
                        <Space
                            handleOpen={handleOpen}
                            list={spaceList}
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
                            <div className="spaceName">
                                {spaceN.name}
                            </div>
                        </List>
                        <Divider />
                        <List>
                            <IconButton
                                className='!ml-3'
                                aria-label="delete"
                                size="small"
                                onClick={handleBack}
                            >
                                <ArrowBackIcon className="!h-6 !w-6"/>
                            </IconButton>

                            <Page
                                idSpace={spaceOpenId}
                                checkText={checkText}
                            />

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
                error={error}
                shared={true}
                placeholder={"Пространство"}
            />
        </div>
    )
}