import React, { useEffect, useState} from "react";
import "../../css/document.css"
import MDEditor, {commands,  ICommand} from "@uiw/react-md-editor";
import Box from "@mui/material/Box";
import Button from '@mui/material/Button';
import EditIcon from '@mui/icons-material/Edit';
import SaveOutlinedIcon from '@mui/icons-material/SaveOutlined';
import {Image} from "./Image";
import Help from "./Help";
import {Pdf} from "./Pdf";

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
    newText: (val:any) => void,
    image: (text:string) => void
}

export const MarkdownDocument = ({save, edit, CloseEdit, OpenEdit, document, newText, image}: Props) =>{

    const [open, setOpen] = useState(false);
    const handleOpen = () => setOpen(true);
    const handleClose = () => setOpen(false);

    const imageCom: ICommand = {
        name: "title3",
        keyCommand: "title3",
        buttonProps: { "aria-label": "Insert title3" },
        icon: (
            <svg width="12" height="12" viewBox="0 0 20 20">
                <path
                    fill="currentColor"
                    d="M15 9c1.1 0 2-.9 2-2s-.9-2-2-2-2 .9-2 2 .9 2 2 2zm4-7H1c-.55 0-1 .45-1 1v14c0 .55.45 1 1 1h18c.55 0 1-.45 1-1V3c0-.55-.45-1-1-1zm-1 13l-6-5-2 2-4-5-4 8V4h16v11z"
                ></path>
            </svg>
        ),
        execute: () => {
            handleOpen()
            console.log(commands.getCommands())
        }
    };

    const { height } = useWindowDimensions()

    return (
        <div data-color-mode="light">
            <Image
                image={image}
                open={open}
                handleClose={handleClose}
            />
            <Box className='buttons'>
                <Help/>
                <Pdf/>
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
                            commands={[
                                commands.bold,
                                commands.italic,
                                commands.strikethrough,
                                commands.hr,
                                commands.group(
                                    [
                                        commands.title1,
                                        commands.title2,
                                        commands.title3,
                                        commands.title4,
                                        commands.title5,
                                        commands.title6
                                    ],
                                    {
                                        name: "title",
                                        groupName: "title",
                                        buttonProps: { "aria-label": "Insert title" }
                                    }
                                ),
                                commands.divider,
                                commands.link,
                                commands.quote,
                                commands.code,
                                commands.codeBlock,
                                imageCom,
                                commands.divider,
                                commands.unorderedListCommand,
                                commands.orderedListCommand,
                                commands.checkedListCommand
                            ]}
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
                            commands={[
                                commands.bold,
                                commands.italic,
                                commands.strikethrough,
                                commands.hr,
                                commands.group(
                                    [
                                        commands.title1,
                                        commands.title2,
                                        commands.title3,
                                        commands.title4,
                                        commands.title5,
                                        commands.title6
                                    ],
                                    {
                                        name: "title",
                                        groupName: "title",
                                        buttonProps: { "aria-label": "Insert title" }
                                    }
                                ),
                                commands.divider,
                                commands.link,
                                commands.quote,
                                commands.code,
                                commands.codeBlock,
                                imageCom,
                                commands.divider,
                                commands.unorderedListCommand,
                                commands.orderedListCommand,
                                commands.checkedListCommand

                            ]}
                        />
                )}
            </Box>
        </div>
    );
}