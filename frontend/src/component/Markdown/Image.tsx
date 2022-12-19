import React, { useEffect, useState} from "react";
import Button from "@mui/material/Button";
import Box from '@mui/material/Box';
import Modal from '@mui/material/Modal';
import {useParams} from "react-router-dom";
import {PhotoCamera} from "@mui/icons-material";
import {Input} from "reactstrap";
import {CircularProgress, ImageList, Snackbar} from "@mui/material";
import Menu from "@mui/material/Menu";
import DeleteIcon from "@mui/icons-material/Delete";

const styleB = {
    backgroundColor: '#4FB5D7',
};

const style = {
    position: 'absolute' as 'absolute',
    top: '50%',
    left: '50%',
    transform: 'translate(-50%, -50%)',
    width: 700,
    height: 350,
    backgroundColor: '#FCFCFC',
    borderStyle: 'solid',
    borderWidth: 3,
    borderColor: '#4FB5D7',
    borderRadius: 5,
    boxShadow: 24,
    p: 2,
};

type Props ={
    image: (text:string) => void,
    open: any,
    handleClose: () => void
}

interface IComp {
    fileName: string,
    fileGUID: string,
    fileType: string,
    size: any
}

export const Image = ({image, open, handleClose}:Props) =>{

    const { spaceId } = useParams();

    const [loadImage, setLoadImage] = useState(true)
    const [linkImage, setLinkImage] = useState(false)
    const [allImage, setAllImage] = useState(false)
    const [path, setPath] = useState('')
    const [images, setImages] = useState<IComp[]>([])
    const [error, setError] = useState("")

    const [anchorEl, setAnchorEl] = React.useState<null | HTMLElement>(null);
    const openDel = Boolean(anchorEl);
    const [editGuid, setEditGuid] = useState("")

    useEffect(() => {
        getImages()
    }, [setImages])

    const getImages = async () => {
        let response = await fetch('/user/uploads?bunch=0&size=1000');
        let json = await response.json()
        setImages(json)
    }

    const ChangeLoadImage = () =>{
        setLoadImage(true)
        setLinkImage(false)
        setAllImage(false)
        setError("")
    }

    const ChangeLinkImage = () =>{
        setLoadImage(false)
        setLinkImage(true)
        setAllImage(false)
        setError("")
    }

    const ChangeAllImage = () =>{
        setLoadImage(false)
        setLinkImage(false)
        setAllImage(true)
        getImages()
    }

    const handleChange = (e:any) => {
        setPath(e.target.value);
    }

    const handleClick = (event: React.MouseEvent<HTMLButtonElement>, guid:string) => {
        setAnchorEl(event.currentTarget);
        setEditGuid(guid)
    };

    const handleCloseDel = () => {
        setAnchorEl(null);
    };

    const submitForm = async (event: any) => {
        event.preventDefault();
        let files = event.target.files;
        let formData = new FormData();
        formData.append("file", files[0]);
        formData.append("thumbnailHeight", '10');
        formData.append("thumbnailWidth", '10');
        await fetch('/spaces/' + spaceId +'/upload/image', {
            method: 'POST',
            headers: {
                'Accept': 'application/json'
            },
            body: formData
        }).then(async response => {
            if(response.ok){
                let json = await response.json()
                let res = await fetch('/download/image/' + json.fileGUID);
                image('\n![](' + res.url + ')')
                handleClose()
            }
        });
    };

    const submitLink = () => {
        if(!errorLink()){
            image('\n![](' + path + ')')
            setError("")
            handleClose()
            setPath("")
        }
    }

    const errorLink = () =>{
        if(!(/^(?:(?:(?:https?|ftp):)?\/\/)(?:\S+(?::\S*)?@)?(?:(?!(?:10|127)(?:\.\d{1,3}){3})(?!(?:169\.254|192\.168)(?:\.\d{1,3}){2})(?!172\.(?:1[6-9]|2\d|3[0-1])(?:\.\d{1,3}){2})(?:[1-9]\d?|1\d\d|2[01]\d|22[0-3])(?:\.(?:1?\d{1,2}|2[0-4]\d|25[0-5])){2}(?:\.(?:[1-9]\d?|1\d\d|2[0-4]\d|25[0-4]))|(?:(?:[a-z\u00a1-\uffff0-9]-*)*[a-z\u00a1-\uffff0-9]+)(?:\.(?:[a-z\u00a1-\uffff0-9]-*)*[a-z\u00a1-\uffff0-9]+)*(?:\.(?:[a-z\u00a1-\uffff]{2,})).?)(?::\d{2,5})?(?:[/?#]\S*)?$/i.test(path))){
            setError("Ссылка введена некорректно")
            return true
        }
        return  false

    }

    const addImage = (guid:any) => {
        image('\n![](http://localhost:3000/download/image/' + guid + ')')
    }

    const remove = async () => {
        await fetch('/delete/image/' + editGuid, {
            method: 'DELETE',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        });
        handleCloseDel()
        getImages()
    }

    const OList =
        <ImageList sx={{ width: 550, height: '*', maxHeight: 300, position: 'absolute' }} cols={5} rowHeight={10}>
            {images.map((l: any) => (
                <div
                    key={l.fileGUID}
                    onContextMenu={(e) => {
                        e.preventDefault();
                        e.stopPropagation();
                    }}
                >
                    <Button
                        sx={{height:60, width: 100}}
                        variant="text"
                        size="large"
                        onClick={()=> addImage(l.fileGUID)}
                        onContextMenu={(e) => handleClick(e, l.fileGUID)}
                    >
                        <img style={{height: 45, maxWidth:80}} src={'http://localhost:3000/download/thumbnail/'+l.fileGUID}/>
                    </Button>
                </div>
            ))}
        </ImageList>
    ;

    return (
        <div>
            <Modal
                open={open}
                onClose={handleClose}
                aria-labelledby="modal-modal-title"
                aria-describedby="modal-modal-description"
            >
                <Box sx={style}>
                    <Box className='buttonsImage'>
                        {(loadImage ?
                            <Button style={styleB} variant="contained">
                                <div className='textImage'>
                                    Загрузить изображение
                                </div>
                            </Button>
                            :
                            <Button variant="text" onClick={ChangeLoadImage}>
                                <div style={{color: '#747A80'}} className='textImage'>
                                    Загрузить изображение
                                </div>
                            </Button>
                        )}
                        {(linkImage ?
                                <Button style={styleB} variant="contained">
                                    <div className='textImage'>
                                        Вставить ссылку
                                    </div>
                                </Button>
                                :
                                <Button sx={{width: '100%'}} variant="text" onClick={ChangeLinkImage}>
                                    <div style={{color: '#747A80'}} className='textImage'>
                                        Вставить <br/> ссылку
                                    </div>
                                </Button>
                        )}
                        {(allImage ?
                                <Button style={styleB} variant="contained">
                                    <div className='textImage'>
                                        Загруженные изображения
                                    </div>
                                </Button>
                                :
                                <Button variant="text" onClick={ChangeAllImage}>
                                    <div style={{color: '#747A80'}} className='textImage'>
                                        Загруженные изображения
                                    </div>
                                </Button>
                        )}
                    </Box>
                    <Box className='workingAria'>
                        {(loadImage ?
                          <div>
                              <div className='rectangle'/>
                              <Button variant="text" className='camera' color="primary" aria-label="upload picture" component="label" size="large">
                                  <input hidden accept="image/*" type="file" onChange={submitForm} />
                                  <PhotoCamera className='iconCamera' />
                              </Button>
                          </div>
                          :
                          <div/>
                        )}
                        {(linkImage ?
                          <div>
                              <Input
                                  className='addPath'
                                  type="text"
                                  name="name"
                                  id="name"
                                  value={path}
                                  placeholder={'Ссылка'}
                                  onChange={handleChange}
                                  autoComplete="off"
                              />
                              <div className='linkErrorText'>
                                  {error}
                              </div>
                              <Button style={styleB} className='addLink' variant="contained" onClick={submitLink}>
                                  <div className='textImage'>
                                      Добавить
                                  </div>
                              </Button>
                          </div>
                          :
                          <div/>
                        )}
                        {(allImage ?
                            <div>
                                {OList}
                            </div>
                           :
                           <div/>
                        )}
                    </Box>
                </Box>
            </Modal>
            <Menu
                id="basic-menu"
                anchorEl={anchorEl}
                open={openDel}
                onClose={handleCloseDel}
                MenuListProps={{
                    'aria-labelledby': 'basic-button',
                }}
            >
                <Button sx={{
                    height: 25,
                    width:'100%',
                    alignItems: 'left',
                    justifyContent: 'left'
                }}
                        variant="text"
                        onClick={remove}
                >
                    <DeleteIcon className="delIcon"/>
                    &emsp;
                    <div className="delText">
                        Удалить
                    </div>
                </Button>

            </Menu>
        </div>
    );
}