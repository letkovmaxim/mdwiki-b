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
                    sx={{
                        alignItems: 'left',
                        justifyContent: 'left'
                    }}
                    className="buttonSpace"
                    variant="text"
                    size="small"
                    onClick={() => toPage(l.id, l.name, l.shared)}
                    onContextMenu={(e) => handleClickMenu(e, l.name, l.shared, l.id)}
                >
                    <FolderOpenOutlinedIcon sx = {{color: '#747A80', height: '18px', width: '18px'}}/>
                    <div>&emsp;</div>
                    <div className='textButton'>
                        {l.name}
                    </div>
                </Button>
            </div>
        )
    });

    return(
        <div>
            <List>
                <div className="headerText">
                    Пространства
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
                    onClick={handleOpen}
                >
                    <AddIcon sx={{ height:25, width:25}}/>
                </IconButton>
                <Box className='box'>
                    {OList}
                </Box>

            </List>

            <Menu
                sx={{
                    left: '10px'
                }}
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
                    variant="text"
                    onClick={handleCloseMenuForEdit}
                >
                    <EditIcon className="icon"sx={{color: '#747A80'}}/>
                    &emsp;
                    <div className="textContextMenu">
                        Изменить
                    </div>
                </Button>
                <br />
                <Button sx={{
                    width:'100%',
                    alignItems: 'left',
                    justifyContent: 'left'
                }}
                        variant="text"
                        onClick={remove}
                >
                    <DeleteIcon className="icon"/>
                    &emsp;
                    <div className="textContextMenu">
                        Удалить
                    </div>
                </Button>
            </Menu>
        </div>
    )
}