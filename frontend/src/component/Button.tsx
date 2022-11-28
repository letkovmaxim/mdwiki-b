import * as React from 'react';
import Button from '@mui/material/Button';
import Menu from '@mui/material/Menu';
import MenuItem from '@mui/material/MenuItem';
import "../css/hello.css"

export default function Contacts() {
    const [anchorEl, setAnchorEl] = React.useState<null | HTMLElement>(null);
    const open = Boolean(anchorEl);
    const handleClick = (event: React.MouseEvent<HTMLButtonElement>) => {
        setAnchorEl(event.currentTarget);
    };
    const handleClose = () => {
        setAnchorEl(null);
    };

    const styleContacts = {
        backgroundColor: '#4FB5D7',
        borderStyle: 'solid',
        borderWidth: 3,
        borderColor: '#FCFCFC',
        borderRadius: 20,
        marginLeft: '1%',
        marginTop: '1%',
    };

    return (
        <div>
            <Button
                sx={{
                    position: 'absolute'
                }}
                style={styleContacts}
                variant="contained"
                id="basic-button"
                size="small"
                aria-controls={open ? 'basic-menu' : undefined}
                aria-haspopup="true"
                aria-expanded={open ? 'true' : undefined}
                onClick={handleClick}
            >
                <div className="buttonText">
                    КОНТАКТЫ
                </div>
            </Button>
            <Menu
                id="basic-menu"
                anchorEl={anchorEl}
                open={open}
                onClose={handleClose}
                MenuListProps={{
                    'aria-labelledby': 'basic-button',
                }}
            >
                <MenuItem onClick={handleClose}>Телефон: +7-900-424-43-43</MenuItem>
                <MenuItem onClick={handleClose}>Email: wiki-service@mail.ru</MenuItem>
            </Menu>
        </div>
    );
}