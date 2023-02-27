import React from 'react';
import Box from "@mui/material/Box";
import LockIcon from '@mui/icons-material/Lock';

export const Error = () => {
    return(
        <div className="max-w-full max-h-full mt-40">
            <Box className="text-center">
                <LockIcon className="!h-40 !w-40 my-4 mx-4 !text-yellow-400"/>
                <div className="text-2xl font-bold text-slate-500">
                    CТРАНИЦА НЕ НАЙДЕНА ИЛИ ДОСТУП ЗАПРЕЩЕН
                </div>
            </Box>
        </div>
    );
}