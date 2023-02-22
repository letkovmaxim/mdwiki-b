import * as React from 'react';
import AlternateEmailOutlinedIcon from '@mui/icons-material/AlternateEmailOutlined';
import Menu from '@mui/material/Menu';
import IconButton from '@mui/material/IconButton';
import "../../css/sidePanel.css"

export default function Contact() {
    const [anchorEl, setAnchorEl] = React.useState<null | HTMLElement>(null);
    const open = Boolean(anchorEl);
    const handleClick = (event: React.MouseEvent<HTMLButtonElement>) => {
        setAnchorEl(event.currentTarget);
    };
    const handleClose = () => {
        setAnchorEl(null);
    };

    return (
        <div>
            <IconButton
                className='right-52'
                onClick={handleClick}
            >
                <AlternateEmailOutlinedIcon/>
            </IconButton>
            <Menu
                id="basic-menu"
                anchorEl={anchorEl}
                open={open}
                onClose={handleClose}
                MenuListProps={{
                    'aria-labelledby': 'basic-button',
                }}
            >
                <div>&emsp;Телефон: +7-900-424-43-43&emsp;</div>
                <div>&emsp;Email: wiki-service@mail.ru&emsp;</div>
            </Menu>
        </div>
    );
}