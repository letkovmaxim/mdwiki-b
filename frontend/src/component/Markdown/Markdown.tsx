import React, { useEffect, useState} from "react";
import "../../css/document.css"
import MDEditor from "@uiw/react-md-editor";
import Box from "@mui/material/Box";
import Button from '@mui/material/Button';
import EditIcon from '@mui/icons-material/Edit';
import SaveOutlinedIcon from '@mui/icons-material/SaveOutlined';

const styleEdit = {
    backgroundColor: '#4FB5D7',
};

function useWindowDimensions() {
    const [height, setHeight] = useState(window.innerHeight);

    const updateWidthAndHeight = () => {
        if(window.innerHeight > 630){
            setHeight(window.innerHeight);
        }else {
            setHeight(630)
        }
    };

    useEffect(() => {
        window.addEventListener("resize", updateWidthAndHeight);
        return () => window.removeEventListener("resize", updateWidthAndHeight);
    });

    return {height}
}

type Props ={
    save: () => void,
    edit: boolean,
    CloseEdit: () => void,
    OpenEdit: () => void,
    document:any,
    newText: (val:any) => void
}

export const Markdown = ({save, edit, CloseEdit, OpenEdit, document, newText}: Props) =>{

    const { height } = useWindowDimensions()

    return (
        <div data-color-mode="light">
            <Box className='buttons'>
                {(edit ?
                        <Button style={styleEdit} className='editButton' variant="contained" onClick={CloseEdit}>
                            <EditIcon className='iconEdit2'/>
                            <div>&emsp;</div>
                            <div className='edit2'>
                                Редактировать
                            </div>
                        </Button>
                        :
                        <Button className='editButton'  variant="outlined" onClick={OpenEdit}>
                            <EditIcon className='iconEdit'/>
                            <div>&emsp;</div>
                            <div className='edit'>
                                Редактировать
                            </div>
                        </Button>
                )}
                <Button className='saveButton' variant="text" onClick={save}>
                    <SaveOutlinedIcon className='iconEdit'/>
                    <div>&emsp;</div>
                    <div className='edit'>
                        Сохранить
                    </div>
                </Button>
            </Box>
            <Box className='markdown'>
                {(edit ?
                        <MDEditor
                            height = {height - 120}
                            minHeight={height - 120}
                            maxHeight ={height - 120}
                            value={document.text}
                            preview="edit"
                            extraCommands={[]}
                            onChange={(val) => newText(val)}
                        />
                        :
                        <MDEditor
                            height = {height - 120}
                            minHeight={height - 120}
                            maxHeight ={height - 120}
                            value={document.text}
                            preview="preview"
                            extraCommands={[]}
                        />
                )}

            </Box>


        </div>
    );
}