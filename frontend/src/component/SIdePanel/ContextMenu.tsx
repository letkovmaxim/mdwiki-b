import * as React from 'react';
import Menu from "@mui/material/Menu";
import Button from "@mui/material/Button";
import AddIcon from "@mui/icons-material/Add";
import EditIcon from "@mui/icons-material/Edit";
import DeleteIcon from "@mui/icons-material/Delete";
import "../../css/sidePanel.css"

type Props ={
    anchorEl:any,
    openMenu: boolean,
    handleCloseMenu: () => void,
    handleCloseMenuForAdd: () => void,
    handleCloseMenuForEdit: () => void,
    remove: () => void
}

export const ContextMenu = ({ anchorEl, openMenu, handleCloseMenu, handleCloseMenuForAdd, handleCloseMenuForEdit, remove}: Props) => {
    return(
        <div>
            <Menu
                className='!left-2.5'
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
                    onClick={handleCloseMenuForAdd}
                >
                    <AddIcon className="iconContextMenu"/>
                    &emsp;
                    Добавить
                </Button>
                <br />
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