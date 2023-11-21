import {useState, useEffect} from 'react';

import {useNavigate} from 'react-router-dom';

import axios from 'axios';

export default function Kakaoauth() {
    const [userInfo, setUserInfo] = useState();
    const navigate = useNavigate();
    const PARAMS = new URL(window.location.href).searchParams;
    const KAKAO_CODE = PARAMS.get("code");
    const [accessTokenFetching, setAccessTokenFetching] = useState(false);
 
    console.log("KAKAO_CODE:", KAKAO_CODE);
    // Access Token 받아오기
    const getAccessToken = async () => {
        if (accessTokenFetching) return; // Return early if fetching
        console.log("getAccessToken 호출");

        try {
            setAccessTokenFetching(true); // Set fetching to true
            const response = await axios.get(
                "http://localhost:8080/login/oauth2/code/kakao",
        {
                    params:{"code":KAKAO_CODE},
                    headers:{"Content-Type": "application/json"}
                }
            );
            const accessToken = response.data.accessToken;
            console.log("accessToken:", accessToken);
 
            // setUserInfo({
            //     ...userInfo,
            //     accessToken: accessToken,
            // });
 
            setAccessTokenFetching(false); // Reset fetching to false
            navigate("/");
        } catch (error) {
            console.error("Error:", error);
            setAccessTokenFetching(false); // Reset fetching even in case of error
        }
    };
 
 
    useEffect(() => {
        if(KAKAO_CODE) {
            getAccessToken();
        }
        // if (KAKAO_CODE && !userInfo.accessToken) {
        //     getAccessToken();
        // }
    }, [KAKAO_CODE]);

    return (
        <div>
           Loading...
        </div>
    );
}