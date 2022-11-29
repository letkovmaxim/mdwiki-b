import React from "react";
import "../css/main.css"
import IconButton from "@mui/material/IconButton";
import MenuIcon from "@mui/icons-material/Menu";
import {Container} from "reactstrap";
import AccountCircleOutlinedIcon from "@mui/icons-material/AccountCircleOutlined";
import Menu from "@mui/material/Menu";
import MenuItem from "@mui/material/MenuItem";
import Button from "@mui/material/Button";

type Props = {
    handleDrawerOpen: () => void,
    open: boolean,
    handleSubmitToLogout: () => void,
    username: string
}

export const Header = ({handleDrawerOpen, open, username, handleSubmitToLogout}: Props) =>{

    const [anchorEl, setAnchorEl] = React.useState<null | HTMLElement>(null);
    const openMenu = Boolean(anchorEl);
    const handleClick = (event: React.MouseEvent<HTMLButtonElement>) => {
        setAnchorEl(event.currentTarget);
    };
    const handleClose = () => {
        setAnchorEl(null);
    };

    return(
        <Container className='AppBar' open={open}>
            <IconButton
                color="inherit"
                onClick={handleDrawerOpen}
                size="small"
                sx={{  ...(open && { display: 'none' }), top:'5px' }}
            >
                <MenuIcon className='menu' />
            </IconButton>

            <Container className="logo">
                <div className="rec1"></div>

                <div className="logoText">
                    WS
                </div>

                <div className="rec2"></div>
            </Container>

            <IconButton
                sx={{
                    position: 'absolute',
                    right: '1%',
                    top: '5px'
                }}
                size="small"
                id="basic-button"
                aria-controls={openMenu ? 'basic-menu' : undefined}
                aria-haspopup="true"
                aria-expanded={openMenu ? 'true' : undefined}
                onClick={handleClick}
            >
                <AccountCircleOutlinedIcon className='menu'/>
            </IconButton>
            <Menu
                id="basic-menu"
                anchorEl={anchorEl}
                open={openMenu}
                onClose={handleClose}
                MenuListProps={{
                    'aria-labelledby': 'basic-button',
                }}
            >
                <MenuItem onClick={handleClose}>{username}</MenuItem>
                <Button color="primary" type="submit" onClick={handleSubmitToLogout}>
                    <div style={{color: '#F34646'}}>
                        ВЫХОД
                    </div>
                </Button>
            </Menu>

        </Container>
    );
}