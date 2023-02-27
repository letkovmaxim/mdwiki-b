import React, {useEffect} from "react";
import {useParams} from "react-router-dom";
import {Box, Button, Modal, FormControl, InputLabel, MenuItem, Select} from "@mui/material";
import PictureAsPdfIcon from '@mui/icons-material/PictureAsPdf';
import { SelectChangeEvent } from '@mui/material/Select';
import "../../css/document.css"
import {useSelector} from "react-redux";

const MenuProps = {
    PaperProps: {
        style: {
            maxHeight: 200,
        },
    },
};

const styleSelect = {
    color: "white",
    '.MuiOutlinedInput-notchedOutline': {
        borderColor: '#FFFFFF',
    },
    '&.Mui-focused .MuiOutlinedInput-notchedOutline': {
        borderColor: '#FFFFFF',
    },
    '&:hover .MuiOutlinedInput-notchedOutline': {
        borderColor: '#FFFFFF',
    },
    '.MuiSvgIcon-root ': {
        fill: "white !important",
    }
}

const fonts = [
    { value: "anonymous", name: "Anonymous" },
    { value: "arial", name: "Arial" },
    { value: "calibri" , name: "Calibri" },
    { value: "liberation", name: "Liberation" },
    { value: "rubik", name: "Rubik" },
    { value: "segoeui", name: "Segoe UI" },
    { value: "times", name: "Times New Roman" },
    { value: "truetypewriter", name: "Truetypewriter" }
];

const fontsSize = [6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22,
    23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38,
    39, 40, 41, 42, 43 ,44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54,
    55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66];

const views = [
    { value: "false", name: "Только текущую страницу" },
    { value: "true", name: "Текущую и под страницы" }
];

export const Pdf  = () => {

    const {spaceId} = useParams();
    const {pageId} = useParams();
    const nameFile = useSelector((state:any) => state.app.namePage)

    const [font, setFont] = React.useState('times');

    const handleChangeFont = (event: SelectChangeEvent) => {
        setFont(event.target.value as string);
    };

    const [fontSize, setFontSize] = React.useState('16');

    const handleChangeFontSize = (event: SelectChangeEvent) => {
        setFontSize(event.target.value as string);
    };

    const [view, setView] = React.useState("false");

    const handleChangeView = (event: SelectChangeEvent) => {
        setView(event.target.value as string);
    };

    const [open, setOpen] = React.useState(false);
    const handleOpen = () => setOpen(true);
    const handleClose = () => {
        setOpen(false)
        setFont('times')
        setFontSize('16')
        setView("false")
    };

    const downloadPdf = async () => {
        fetch('/spaces/' + spaceId + '/pages/' + pageId + '/document/pdf?font=' + font + '&fontSize=' + fontSize + '&tree=' + view).then(response => {
            response.blob().then(blob => {
                console.log(response)
                const fileURL = window.URL.createObjectURL(blob);
                let alink = document.createElement('a');
                alink.href = fileURL;
                alink.download = nameFile + '.pdf';
                alink.click();
            })
            if (response.ok) {
                handleClose()
            }
        })
    }

    const listFronts = fonts.map((front) => {
        return (
            <MenuItem key={front.value} value={front.value}>
                <div className='select'>
                    {front.name}
                </div>
            </MenuItem>
        )
    })

    const listFrontsSize = fontsSize.map((frontSize) => {
        return (
            <MenuItem key={frontSize} value={String(frontSize)}>
                <div className='select'>
                    {frontSize}
                </div>
            </MenuItem>
        )
    })

    const listView = views.map((view) => {
        return (
            <MenuItem key={view.value} value={view.value}>
                <div className='select'>
                    {view.name}
                </div>
            </MenuItem>
        )
    })

    const select = (name: string, list: any, value: any, handleChange: any) => {
        return (
            <Box className='min-w-[120]'>
                <FormControl fullWidth className='max-h-[100]'>
                    <InputLabel id="demo-simple-select-label" className='!text-white' >{name}</InputLabel>
                    <Select
                        sx={styleSelect}
                        className='!h-[40px] !text-white !font-bold'
                        labelId="demo-simple-select-label"
                        id="demo-simple-select"
                        value={value}
                        label={name}
                        onChange={handleChange}
                        MenuProps={MenuProps}
                    >
                        {list}
                    </Select>
                </FormControl>
            </Box>
        )
    }

    return (
        <div>
            <Button className='pdfBtn'  variant="text" onClick={handleOpen}>
                <PictureAsPdfIcon className='editIcon !text-slate-500'/>
                <div>&emsp;</div>
                <div className='mt-[2px]'>Загрузить</div>
            </Button>
            <Modal
                open={open}
                onClose={handleClose}
                aria-labelledby="modal-modal-title"
                aria-describedby="modal-modal-description"
            >
                <Box className='pdfBox'>
                    {select("Шрифт", listFronts, font, handleChangeFont)}
                    <br/>
                    {select("Размер шрифта", listFrontsSize, fontSize, handleChangeFontSize)}
                    <br/>
                    {select("", listView, view, handleChangeView)}
                    <Button variant="text" className='pdfDownBtn' onClick={downloadPdf}>
                        Загрузить
                    </Button>
                </Box>
            </Modal>
        </div>
    )
}