import kakaoPicture from "../assets/kakao.png"

export default function KakaoLogin() {
    const CLIENT_ID = process.env.REACT_APP_KAKAO_REST_API_KEY;
    // 서버 연결 시, env에서 3000이 아니라 8080으로
    const REDIRECT_URI = process.env.REACT_APP_KAKAO_REDIRECT_URI;
    const kakaoURL = `https://kauth.kakao.com/oauth/authorize?client_id=${CLIENT_ID}&redirect_uri=${REDIRECT_URI}&response_type=code`

    return(
        <div>
            <img className="w-[255px] h-[35px]" src={kakaoPicture}
                 alt="카카오 로그인"
                 onClick={() => window.location.href = kakaoURL}
            />
        </div>

    )
}