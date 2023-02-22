import React from "react";
import List from "@mui/material/List";
import Divider from "@mui/material/Divider";
import IconButton from "@mui/material/IconButton";
import AddIcon from "@mui/icons-material/Add";
import Menu from "@mui/material/Menu";
import Button from "@mui/material/Button";
import EditIcon from "@mui/icons-material/Edit";
import DeleteIcon from "@mui/icons-material/Delete";
import FolderOpenOutlinedIcon from '@mui/icons-material/FolderOpenOutlined';
import Box from "@mui/material/Box";
import "../../css/sidePanel.css"

type Props = {
    handleOpen: () => void,
    list: any,
    anchorEl:any,
    openMenu: any,
    handleCloseMenu: () => void,
    handleCloseMenuForEdit: () => void,
    remove: () => void,
    toPage:(id:number, name:string, shared:boolean) => void,
    handleClickMenu:(e:any, name:string, shared:boolean, id:number) => any,
}

export const Space = ({handleOpen, list, anchorEl, openMenu, handleCloseMenu, handleCloseMenuForEdit, remove, toPage, handleClickMenu}:Props) => {

    const OList = list.map((l:any) => {

        return (
            <div
                key={l.id}
                onContextMenu={(e) => {
                    e.preventDefault();
                    e.stopPropagation();
                }}
            >

                <Button
                    className="spaceBtn"
                    variant="text"
                    size="small"
                    onClick={() => toPage(l.id, l.name, l.shared)}
                    onContextMenu={(e) => handleClickMenu(e, l.name, l.shared, l.id)}
                >
                    <FolderOpenOutlinedIcon className="openFolderIcon"/>
                    <div>&emsp;</div>
                    {l.name}
                </Button>
            </div>
        )
    });

    return(
        <div>
            <List>
                <div className="spaceName">
                    Пространства
                </div>
            </List>
            <Divider />
            <List>
                <IconButton
                    className='!ml-3'
                    aria-label="delete"
                    size="small"
                    onClick={handleOpen}
                >
                    <AddIcon className="!h-6 !w-6"/>
                </IconButton>
                <Box className='scrollbar'>
                    {OList}
                </Box>

            </List>

            <Menu
                className="left-2.5"
                id="demo-positioned-menu"
                aria-labelledby="demo-positioned-button"
                anchorEl={anchorEl}
                open={openMenu}
                onClose={handleCloseMenu}
                MenuListProps={{
                    'aria-labelledby': 'basic-button',
                }}
            >
                <Button
                    className='btnContextMenu'
                    variant="text"
                    onClick={handleCloseMenuForEdit}
                >
                    <EditIcon className="iconContextMenu"/>
                    &emsp;
                    Изменить
                </Button>
                <br />
                <Button
                    className='btnContextMenu'
                    variant="text"
                    onClick={remove}
                >
                    <DeleteIcon className="iconContextMenu"/>
                    &emsp;
                    Удалить
                </Button>
            </Menu>
        </div>
    )
}