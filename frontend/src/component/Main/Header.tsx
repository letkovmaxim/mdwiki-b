import React from "react";
import "../../css/main.css"
import IconButton from "@mui/material/IconButton";
import MenuIcon from "@mui/icons-material/Menu";
import {Container} from "reactstrap";
import AccountCircleOutlinedIcon from "@mui/icons-material/AccountCircleOutlined";
import Menu from "@mui/material/Menu";
import Button from "@mui/material/Button";
import LogoutIcon from '@mui/icons-material/Logout';
import {Profile} from "./Profile";
import Box from "@mui/material/Box";

type Props = {
    handleDrawerOpen: () => void,
    open: boolean,
    handleSubmitToLogout: () => void,
}

export const Header = ({handleDrawerOpen, open, handleSubmitToLogout}: Props) =>{

    const [anchorEl, setAnchorEl] = React.useState<null | HTMLElement>(null);
    const openMenu = Boolean(anchorEl);
    const handleClick = (event: React.MouseEvent<HTMLButtonElement>) => {
        setAnchorEl(event.currentTarget);
    };
    const handleClose = () => {
        setAnchorEl(null);
    };

    return(
        <Container className='header' open={open}>

            <Box className="logoBox">
                <div className="rect1Main"></div>

                <div className="wsLogoMain">
                    WS
                </div>

                <div className="rect2Main"></div>
            </Box>

            <IconButton
                className="absolute left-2 top-[5px]"
                color="inherit"
                onClick={handleDrawerOpen}
                size="small"
                sx={{  ...(open && { display: 'none' }), top:'5px' }}
            >
                <MenuIcon className='iconMenu' />
            </IconButton>

            <IconButton
                className="!absolute right-2 top-[5px]"
                size="small"
                id="basic-button"
                aria-controls={openMenu ? 'basic-menu' : undefined}
                aria-haspopup="true"
                aria-expanded={openMenu ? 'true' : undefined}
                onClick={handleClick}
            >
                <AccountCircleOutlinedIcon className='iconMenu'/>
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
                <Profile/>
                <Button className="!text-red-600 !w-full" color="primary" type="submit" onClick={handleSubmitToLogout}>
                    <LogoutIcon className="!h-5 !text-red-600"/>
                    &emsp;ВЫХОД
                </Button>
            </Menu>

        </Container>
    );
}