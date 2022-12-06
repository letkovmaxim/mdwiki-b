import * as React from 'react';
import Menu from "@mui/material/Menu";
import Button from "@mui/material/Button";
import AddIcon from "@mui/icons-material/Add";
import EditIcon from "@mui/icons-material/Edit";
import DeleteIcon from "@mui/icons-material/Delete";


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
                    onClick={handleCloseMenuForAdd}
                >
                    <AddIcon className="icon"sx={{color: '#747A80'}}/>
                    &emsp;
                    <div className="textContextMenu">
                        Добавить
                    </div>
                </Button>
                <br />
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