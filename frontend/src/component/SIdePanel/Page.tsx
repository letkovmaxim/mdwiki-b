import React, {useEffect, useState} from "react";
import AddIcon from "@mui/icons-material/Add";
import IconButton from "@mui/material/IconButton";
import TreeView from '@mui/lab/TreeView';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import ChevronRightIcon from '@mui/icons-material/ChevronRight';
import CustomTreeItem from './CustomTreeItem'
import {ContextMenu} from "./ContextMenu";
import Button from "@mui/material/Button";
import DescriptionOutlinedIcon from '@mui/icons-material/DescriptionOutlined';
import Box from "@mui/material/Box";
import {ModalWindow} from "./Modal";
import { useParams } from "react-router-dom";
import Modal from "@mui/material/Modal";
import Typography from "@mui/material/Typography";
import {useDispatch, useSelector} from "react-redux";
import {getPages, openSpace, treePages} from "../../redux/actions";

type Props = {
    idSpace: number,
    checkText: string
}

interface IComp {
    createdAt: any,
    id: number,
    name: string,
    shared: boolean,
    subpages?: readonly IComp[],
    updatedAt: any
}

const style = {
    position: 'absolute' as 'absolute',
    top: '50%',
    left: '50%',
    transform: 'translate(-50%, -50%)',
    width: '350px',
    height: '80px',
    backgroundColor: '#4FB5D7',
    borderStyle: 'solid',
    borderWidth: 3,
    borderColor: '#FCFCFC',
    borderRadius: 5,
    boxShadow: 24,
    p: 2,
};

export const Page = ({idSpace, checkText}:Props) =>{

    const dispatch = useDispatch()
    const pageList = useSelector((state:any) => state.app.pages)
    const treeP = useSelector((state:any) => state.app.tree)
    const spaceOp = useSelector((state:any) => state.app.openSpace)

    const { pageId } = useParams();
    const { spaceId } = useParams();

    const[idTree, setIdTree] = useState<string[]>([])
    const [lastId, setLastId] = useState(0)

    const[editId, setEditId] = useState<number>()

    const[styles, setStyles] = useState("addSpace")

    const[error, setError] = useState("")

    const[newObject, setNewObject] = useState({
        name: '',
        shared: true
    })

    const [openSave, setOpenSave] = React.useState(false);

    const [redirectId, setRedirectId] = useState(0)

    const handleOpenSave = () => setOpenSave(true);

    const[document, setDocument] = useState({
        text: checkText
    });

    const[addSub, setAddSub] = useState(false)

    const [open, setOpen] = React.useState(false);
    const handleOpen = () => setOpen(true);
    const handleClose = () => {
        setOpen(false);
        setEditId(undefined)
        setNewObject({
            name: '',
            shared: true
        })
        setAddSub(false)
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

    const handleCloseMenuForAdd = () => {
        setAnchorEl(null);
        setOpen(true);
        setAddSub(true)
        setNewObject({
            name: '',
            shared: true
        })
    };

    const handleChange = (e:any) => {
        setNewObject({
            ...newObject,
            [e.target.name]: e.target.value
        });
    }

    useEffect(() => {
        treeOpen()
    }, [])

    const treeOpen = () => {
        if(spaceOp !== String(idSpace)){
            dispatch(treePages([]))
            dispatch(openSpace(String(idSpace)))
        }else {
            setIdTree(treeP)
        }
        getList()
    }

    async function getList() {
        dispatch<any>(getPages(idSpace))
    }

    async function handleSubmit() {
        if(!errorEmpty()){
            await fetch('/spaces/' + idSpace + '/pages' + (editId ? '/' + editId : ''), {
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
                    setError("Страница уже существует")
                }
            });
        }
    }

    async function AddSubpage() {
        if(!errorEmpty()){
            await fetch('/spaces/' + idSpace + '/pages/' + editId, {
                method: 'POST',
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
                    setError("Страница уже существует")
                }
            });
        }
    }


    async function backParent(response: any){
        if(response.ok){
            let json = await response.json()
            window.location.replace("/wiki/space/" + idSpace +"/page/" + json.id);
        }else {
            window.location.replace("/wiki/space/" + idSpace);
        }
    }

    async function remove() {
        if(String(editId) === pageId) {
            let response = await fetch("/spaces/" + idSpace + "/pages/" + pageId + "/parent");

            await fetch('/spaces/' + idSpace + '/pages/' + editId, {
                method: 'DELETE',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                }
            });
            getList()
            handleCloseMenu()

            await backParent(response)
        }else {
            await fetch('/spaces/' + idSpace + '/pages/' + editId, {
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

    async function save(){
        let newDoc = false
        let response = await fetch('/spaces/' + spaceId + '/pages/' + pageId + '/document');
        if(!response.ok){
            newDoc = true
        }
        await fetch('/spaces/' + spaceId + '/pages/' + pageId + '/document', {
            method: (newDoc ? 'POST' : 'PUT'),
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(document),
        })
        redirect(redirectId)
    }

    const checkSave = async (id: number) => {
        let response = await fetch('/spaces/' + spaceId + '/pages/' + pageId + '/document');
        if (response.ok) {
            let json = await response.json()
            if (json.text !== checkText) {
                handleOpenSave()
            } else {
                redirect(id)
            }
        } else {
            if (checkText !== "") {
                handleOpenSave()
            } else {
                redirect(id)
            }
        }
    }

    const toPage = async (id: number) => {
        setDocument({
            text: checkText
        })
        dispatch(treePages(idTree))
        setRedirectId(id)
        if (pageId !== undefined) {
            await checkSave(id)
        } else {
            redirect(id)
        }
    }

    const redirect = (id: number) => {
        window.location.replace("/wiki/space/" + idSpace + "/page/" + id);
    }

    const tree = ( id:number, i:number) => {
        if(i === 1 && lastId != 0){
            if(idTree.includes(String(lastId))){
                const arr = idTree.filter((id) => id !== String(lastId));
                setIdTree(arr)
            }else{
                setIdTree([...idTree, String(lastId)])
            }
        }
        setLastId(id)
    }

    const renderTree = (nodes: any, i:number) => {
        return(

            <CustomTreeItem key={String(nodes.id)} nodeId={String(nodes.id)}   onClickCapture={() => tree(nodes.id, i)}
                            label={
                                <Button
                                    sx={{
                                        alignItems: 'left',
                                        justifyContent: 'left',
                                        minWidth: '100%'
                                    }}
                                    className="buttonPage"
                                    variant="text"
                                    size="small"

                                    onClick={()=> toPage(nodes.id)}
                                    onContextMenu={(e) => handleClickMenu(e, nodes.name, nodes.shared, nodes.id)}
                                >
                                    {(String(nodes.id) === pageId ?  <DescriptionOutlinedIcon sx={{color: '#4FB5D7'}} className='description'/> :  <DescriptionOutlinedIcon className='description'/> )}
                                    <div>&emsp;</div>
                                    {(String(nodes.id) === pageId ? <div style={{marginTop: '3px', color: '#4FB5D7'}}>{nodes.name}</div> : <div className='textButton'>{nodes.name}</div>)}
                                </Button>
                            }
            >
                {Array.isArray(nodes.subpages)
                    ? nodes.subpages.map((node:any) => renderTree(node,++i))
                    : null}
            </CustomTreeItem>
        );
    }


    const OList = pageList.map((l:any) => {
        let i = 0;
        return (
            <div  key={l.id}
                  onContextMenu={(e) => {
                      e.preventDefault();
                      e.stopPropagation();
                  }}>
                {renderTree(l, i)}
            </div>
        )
    });

    return(
        <div>
            <IconButton
                sx={{
                    position: 'absolute',
                    marginLeft: '55px',
                    marginTop: '-45px'
                }}
                aria-label="delete"
                size="small"
                onClick={handleOpen}
            >
                <AddIcon sx={{ height:25, width:25}}/>
            </IconButton>

            <Box className='box'>
                <TreeView
                    aria-label="rich object"
                    defaultCollapseIcon={<ExpandMoreIcon sx={{color: '#747A80'}} />}
                    defaultExpanded={treeP}
                    defaultExpandIcon={<ChevronRightIcon sx={{color: '#747A80'}} />}
                >
                    {OList}
                </TreeView>
            </Box>

            <ContextMenu
                anchorEl={anchorEl}
                openMenu={openMenu}
                handleCloseMenu={handleCloseMenu}
                handleCloseMenuForAdd={handleCloseMenuForAdd}
                handleCloseMenuForEdit={handleCloseMenuForEdit}
                remove={remove}
            />

            <ModalWindow
                open={open}
                handleClose={handleClose}
                styles={styles}
                newObject={newObject}
                handleChange={handleChange}
                addSub={addSub}
                AddSubpage={AddSubpage}
                handleSubmit={handleSubmit}
                editId={editId}
                error={error}
            />

            <Modal
                open={openSave}
                aria-labelledby="modal-modal-title"
                aria-describedby="modal-modal-description"
            >
                <Box sx={style}>
                    <Typography id="modal-modal-title" variant="h6" component="h2">
                        <div style={{color:"white"}}>
                            Последние изменения не сохранены
                        </div>
                    </Typography>
                    <Button variant="text" sx={{position: 'absolute', bottom: '0px', right: '135px'}} onClick={save}>
                        <div style={{color:"white"}}>
                            Сохранить
                        </div>
                    </Button>
                    <Button variant="text" sx={{position: 'absolute', bottom: '0px', right: '10px'}} onClick={() => redirect(redirectId)}>
                        <div style={{color:"white"}}>
                            Продолжить
                        </div>
                    </Button>
                </Box>
            </Modal>
        </div>
    )
}