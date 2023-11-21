import picture3 from "../assets/picture3.jpg"

import {useState} from "react";

import Modal from '@mui/material/Modal';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';

const style = {
    position: 'absolute' as 'absolute',
    top: '50%',
    left: '50%',
    transform: 'translate(-50%, -50%)',
    width: 400,
    height : 350,
    bgcolor: 'background.paper',
    border: '2px solid #000',
    boxShadow: 24,
    p: 4,
  };

export default function Posts() {
    const [explanation, setExplnation] = useState<string>("입력하기...")
    const [showDetailModal, setShowDetailModal] = useState<boolean>(false);
    const [DetailItem, setDetailItem] = useState<string>("");

    const handleClose = () => setShowDetailModal(false);

    const checkDetail = () => {
        if(DetailItem.length > 8) {
            let strSlice = DetailItem.slice(0,8) + "...";
            setExplnation(strSlice);
        }
        else if(!DetailItem) {
            setExplnation("입력하기...")
        }
        else {
            setExplnation(DetailItem);
        }
        handleClose();
    }

    const onChange = (e: React.ChangeEvent<HTMLTextAreaElement>):void => {
        setDetailItem(e.target.value)
    }

    return (
        <div className="flex bg-slate-300">
            <div className="flex flex-col pb-3 items-center justify-center w-screen h-screen">
                <div className="pb-3 text-lg font-bold">상품 등록</div>
                <div className="flex w-[550px] h-[550px] bg-white shadow-sharp rounded-md">
                    <div className="flex w-6/12 float-left rounded-md" style={{ minHeight:"80%", backgroundImage: `url(${picture3})`, backgroundSize: 'cover', backgroundPosition: 'center' }}/>
                    <div className="flex flex-col w-6/12 float-right p-4">
                        <div className="flex flex-col">
                            <strong className="text-base">상품 이름</strong>
                            <input
                                className="pt-3 text-sm bg-transparent border-b-2 border-solid border-gray-300 text-gray-500 box-border font-arvo h-50 w-400 focus:outline-none"
                                type="text"
                                placeholder="ex) 텐트"
                            />
                        </div>
                        <div className="flex flex-col pt-4">
                            <strong className="text-base">상품 설명</strong>
                            {/* 입력하기 버튼 누르면 모달창으로 넘어가서 상품 설명 개시하기. */}
                            <button onClick={()=> setShowDetailModal(true)} className="mt-3 text-sm border rounded-md border-solid border-gray-300 px-4 py-2 text-gray-700">
                                {explanation}
                            </button>
                        </div>
                        <div className="flex flex-col pt-4">
                            <strong className="text-base">카테고리 설정</strong>
                        </div>
                    </div>
                </div>
            </div>
<Modal
  open={showDetailModal}
  aria-labelledby="modal-modal-title"
  aria-describedby="modal-modal-description"
  onClose={()=>{
    handleClose()
    checkDetail()
}}
>
  <Box sx={style}>
    <Typography id="modal-modal-title" variant="h6" component="h2">
      상품 설명
    </Typography>
    <Typography id="modal-modal-description" sx={{ mt: 2 }}>
      <textarea
        className="w-full h-[200px]"
        placeholder="상품 설명을 입력하시오..."
        value={DetailItem}
        onChange={onChange}
      />
      <button onClick={checkDetail}>확인</button>
    </Typography>
  </Box>
</Modal>

        </div>
    )
}