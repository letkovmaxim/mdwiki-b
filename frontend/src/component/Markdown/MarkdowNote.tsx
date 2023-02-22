import React, { useEffect, useState} from "react";
import "../../css/document.css"
import MDEditor from "@uiw/react-md-editor";
import Box from "@mui/material/Box";
import Button from '@mui/material/Button';
import EditIcon from '@mui/icons-material/Edit';
import SaveOutlinedIcon from '@mui/icons-material/SaveOutlined';
import Help from "./Help";

function useWindowDimensions() {
    const [height, setHeight] = useState(window.innerHeight > 630 ? window.innerHeight : 630);

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

export const MarkdowNote = ({save, edit, CloseEdit, OpenEdit, document, newText}: Props) =>{

    const { height } = useWindowDimensions()

    return (
        <div data-color-mode="light">
            <Box className='buttonsBox'>
                <Help/>
                {(edit ?
                        <Button className='editBtn !bg-blue-470 !text-white !text-xs' variant="contained" onClick={CloseEdit}>
                            <EditIcon className='editIcon !text-white'/>
                            <div>&emsp;</div>
                            Редактировать
                        </Button>
                        :
                        <Button className='editBtn !text-xs !text-slate-500'  variant="outlined" onClick={OpenEdit}>
                            <EditIcon className='editIcon !text-slate-500'/>
                            <div>&emsp;</div>
                            Редактировать
                        </Button>
                )}
                <Button className='saveBtn' variant="text" onClick={save}>
                    <SaveOutlinedIcon className='editIcon !text-slate-500'/>
                    <div>&emsp;</div>
                        Сохранить
                </Button>
            </Box>
            <Box className='markdownBox'>
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