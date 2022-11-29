import React, { useEffect, useState} from "react";
import {useParams} from "react-router-dom";
import "../css/document.css"
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
        setHeight(window.innerHeight);
    };

    useEffect(() => {
        window.addEventListener("resize", updateWidthAndHeight);
        return () => window.removeEventListener("resize", updateWidthAndHeight);
    });

    return {height}
}

export const Document = () =>{

    const {spaceId} = useParams();
    const { pageId } = useParams();

    const[newDoc, setNewDoc] = useState(true);

    const[document, setDocument] = useState({
        text: ''
    });

    const[edit, setEdit] = useState(false);

    const { height } = useWindowDimensions()

    const CloseEdit = () => setEdit(false);
    const OpenEdit = () => setEdit(true);


    useEffect(() => {
         getText()
    }, [setDocument])

    async function getText(){
        let response = await fetch('/spaces/' + spaceId + '/pages/' + pageId + '/document');
        if(response.ok){
            let json = await response.json()
            setDocument({
                text: json.text
            })
            setNewDoc(false)
        }
    }

    async function save(){

        await fetch('/spaces/' + spaceId + '/pages/' + pageId + '/document', {
            method: (newDoc ? 'POST' : 'PUT'),
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(document),
        })
    }

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
                            onChange={(val) => {
                                setDocument({text: val!})
                            }}
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