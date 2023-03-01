import React from "react";

export class NotFound extends React.Component<any, any>{
    render() {
        return (
            <div className='w-full h-screen bg-blue-470 text-white/80'>
                <div className='absolute my-10 px-20'>
                    <span className="text-[165px] font-bold">
                        #404
                    </span>
                    <br/>
                    <span className='text-3xl font-bold'>
                        СТРАНИЦА НЕДОСТУПНА
                    </span>
                </div>
            </div>
        );
    }
}