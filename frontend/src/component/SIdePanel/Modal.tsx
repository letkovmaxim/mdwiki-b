import * as React from 'react';
import Button from "@mui/material/Button";
import Modal from "@mui/material/Modal";
import Box from "@mui/material/Box";
import {Input} from "reactstrap";
import FormControl from "@mui/material/FormControl";
import RadioGroup from "@mui/material/RadioGroup";
import FormControlLabel from "@mui/material/FormControlLabel";
import Radio from "@mui/material/Radio";

type Props ={
    open: boolean,
    handleClose: () => void,
    styles: any,
    newObject: any,
    handleChange:(e:any)=>void,
    addSub:boolean,
    AddSubpage: () => void,
    handleSubmit: () => void
    editId: any,
    error: any,
    shared: boolean,
    placeholder: string
}

const style = {
    position: 'absolute' as 'absolute',
    top: '50%',
    left: '50%',
    transform: 'translate(-50%, -50%)',
    width: '300px',
    height: '130px',
    backgroundColor: '#4FB5D7',
    borderStyle: 'solid',
    borderWidth: 3,
    borderColor: '#FCFCFC',
    borderRadius: 5,
    boxShadow: 24,
    p: 2,
};

export const ModalWindow = ({open, handleClose, styles,newObject, handleChange, addSub, AddSubpage, handleSubmit, editId, error, shared, placeholder}: Props) => {
    return(
        <div>
            <Modal
                open={open}
                onClose={handleClose}
                aria-labelledby="modal-modal-title"
                aria-describedby="modal-modal-description"
            >
                <Box sx={style}>
                    <Input
                        className={styles}
                        type="text"
                        name="name"
                        id="name"
                        placeholder={placeholder}
                        value={newObject.name}
                        onChange={handleChange}
                        autoComplete="off"
                    />
                    <div style={{color:"red", fontWeight: "bold"}}>
                        {error}
                    </div>
                    <FormControl
                        sx={{
                            top: '8px'
                        }}
                    >
                        <RadioGroup
                            aria-labelledby="demo-controlled-radio-buttons-group"
                            name="shared"
                            value={newObject.shared}
                            onChange={handleChange}
                        >
                            {shared &&
                            <FormControlLabel className="text" value={true} control={<Radio
                                sx={{color: '#FFFFFF', '&.Mui-checked': {color: '#8482FF',},}} size="small"/>}
                                              label="публичный"/>
                            }

                            <FormControlLabel className="text" value={false} control={<Radio
                                sx={{color: '#FFFFFF', '&.Mui-checked': {color: '#8482FF',},}} size="small"/>}
                                              label="приватный"/>
                        </RadioGroup>
                    </FormControl>
                    {(addSub ?
                            <Button
                                sx={{
                                    position: 'absolute',
                                    bottom: '0px',
                                    right: '10px'
                                }}
                                variant="text"
                                size="small"
                                onClick={AddSubpage}
                            >
                                <div className="text" >Создать</div>
                            </Button>
                            :
                            <Button
                                sx={{
                                    position: 'absolute',
                                    bottom: '0px',
                                    right: '10px'
                                }}
                                variant="text"
                                size="small"
                                onClick={handleSubmit}
                            >
                                {(!editId ? <div className="text" >Создать</div> : <div className="text" >Изменить</div>)}
                            </Button>
                    )}
                </Box>
            </Modal>
        </div>
    )

}