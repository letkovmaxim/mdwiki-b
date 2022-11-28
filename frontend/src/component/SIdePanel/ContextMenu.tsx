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

            {/*<Modal*/}
            {/*    open={open}*/}
            {/*    onClose={handleClose}*/}
            {/*    aria-labelledby="modal-modal-title"*/}
            {/*    aria-describedby="modal-modal-description"*/}
            {/*>*/}
            {/*    <Box sx={style}>*/}
            {/*        <Input*/}
            {/*            className={styles}*/}
            {/*            type="text"*/}
            {/*            name="name"*/}
            {/*            id="name"*/}
            {/*            value={newObject.name}*/}
            {/*            onChange={handleChange}*/}
            {/*            autoComplete="name"*/}
            {/*        />*/}
            {/*        <FormControl*/}
            {/*            sx={{*/}
            {/*                top: '8px'*/}
            {/*            }}*/}
            {/*        >*/}
            {/*            <RadioGroup*/}
            {/*                aria-labelledby="demo-controlled-radio-buttons-group"*/}
            {/*                name="shared"*/}
            {/*                value={newObject.shared}*/}
            {/*                onChange={handleChange}*/}
            {/*            >*/}
            {/*                <FormControlLabel className="text" value={true} control={<Radio*/}
            {/*                    sx={{color: '#FFFFFF', '&.Mui-checked': {color: '#8482FF',},}} size="small"/>}*/}
            {/*                                  label="public"/>*/}

            {/*                <FormControlLabel className="text" value={false} control={<Radio*/}
            {/*                    sx={{color: '#FFFFFF', '&.Mui-checked': {color: '#8482FF',},}} size="small"/>}*/}
            {/*                                  label="private"/>*/}
            {/*            </RadioGroup>*/}
            {/*        </FormControl>*/}
            {/*        {(addSub ?*/}
            {/*                <Button*/}
            {/*                    sx={{*/}
            {/*                        position: 'absolute',*/}
            {/*                        bottom: '0px',*/}
            {/*                        right: '10px'*/}
            {/*                    }}*/}
            {/*                    variant="text"*/}
            {/*                    size="small"*/}
            {/*                    onClick={AddSubpage}*/}
            {/*                >*/}
            {/*                    <div className="text" >Создать</div>*/}
            {/*                </Button>*/}
            {/*                :*/}
            {/*                <Button*/}
            {/*                    sx={{*/}
            {/*                        position: 'absolute',*/}
            {/*                        bottom: '0px',*/}
            {/*                        right: '10px'*/}
            {/*                    }}*/}
            {/*                    variant="text"*/}
            {/*                    size="small"*/}
            {/*                    onClick={handleSubmit}*/}
            {/*                >*/}
            {/*                    {(!editId ? <div className="text" >Создать</div> : <div className="text" >Изменить</div>)}*/}
            {/*                </Button>*/}
            {/*        )}*/}
            {/*    </Box>*/}
            {/*</Modal>*/}
        </div>
    )

}