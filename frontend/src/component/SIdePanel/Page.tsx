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

type Props = {
    idSpace: number
}

interface IComp {
    createdAt: any,
    id: string,
    name: string,
    shared: boolean,
    subpages?: readonly IComp[],
    updatedAt: any
}

export const Page = ({idSpace}:Props) =>{

    const { login } = useParams();
    const { pageId } = useParams();

    const[idTree, setIdTree] = useState<string[]>([])
    const [lastId, setLastId] = useState(0)

    const[list, setList] = useState<IComp[]>([])

    const[editId, setEditId] = useState<number>()

    const[styles, setStyles] = useState("addSpace")

    const[newObject, setNewObject] = useState({
        name: '',
        shared: true
    })

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
        getList()
    }, [setList])



    const treeOpen = () => {
        if(localStorage.getItem('space') !== String(idSpace)){
            localStorage.setItem("tree", JSON.stringify([]))
            localStorage.setItem('space', String(idSpace))
        }else {
            setIdTree(JSON.parse(localStorage.getItem('tree')!))
        }

    }

    async function getList() {
        let response = await fetch('/spaces/' + idSpace + '/pages?bunch=0&size=1000');
        let json = await response.json()
        setList(json)
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
            });
            handleClose()
            handleCloseMenu()
            getList();
            setEditId(undefined)
            setNewObject({
                name: '',
                shared: true
            })
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
            });
            handleClose()
            handleCloseMenu()
            getList();
            setEditId(undefined)
            setNewObject({
                name: '',
                shared: true
            })
        }
    }

    async function backParent(){
        let response = await fetch("/spaces/" + idSpace + "/pages/" + pageId + "/parent");
        if(response.ok){
            let json = await response.json()
            window.location.replace("/wiki/" + login +"/space/" + idSpace +"/page/" + json.id);
        }else {
            window.location.replace("/wiki/" + login +"/space/" + idSpace);
        }
    }

    async function remove() {
        if(editId === Number(pageId)){
            backParent()
        }
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

    const errorEmpty = () => {

        let error = false;

        if(newObject.name.length === 0){
            setStyles("addSpaceError")
            error = true;
        }

        return error;
    }



    const toPage = (id:number) => {
        if(idTree.includes(String(lastId))){
            const arr = idTree.filter((id) => id !== String(lastId));
            setIdTree(arr)
        }else{
            setIdTree([...idTree, String(lastId)])
        }

        localStorage.setItem('tree', JSON.stringify(idTree))
        //console.log(localStorage.getItem('tree'))

        window.location.replace("/wiki/" + login + "/space/" + idSpace +"/page/" + id);
    }


    const tree = ( id:number, i:number) => {
        console.log(idTree)
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

            <CustomTreeItem key={nodes.id} nodeId={nodes.id}   onClickCapture={() => tree(nodes.id, i)}
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
                        <DescriptionOutlinedIcon className='description'/>
                        <div>&emsp;</div>
                        <div className='textButton'>
                            {nodes.name}
                        </div>
                    </Button>
                }
            >
                {Array.isArray(nodes.subpages)
                    ? nodes.subpages.map((node:any) => renderTree(node,++i))
                    : null}
            </CustomTreeItem>
        );
    }


    const OList = list.map((l:any) => {
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
                    defaultExpanded={JSON.parse(localStorage.getItem('tree')!)}

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
            />
        </div>
    )
}