import { http, HttpResponse } from 'msw'
import tent1 from "../assets/mockPicture/tent1.jpg"
import tent2 from "../assets/mockPicture/tent2.jpg"
import tent3 from "../assets/mockPicture/tent3.jpg"
import tent4 from "../assets/mockPicture/tent4.jpg"
import tent5 from "../assets/mockPicture/tent5.jpg"
import tent6 from "../assets/mockPicture/tent6.jpg"
 
export const handlers = [
  http.get('/api/item', ()=>{
    return HttpResponse.json([
      {
        id : 1,
        name : "A형 텐트",
        category : "텐트",
        image : tent1,
        price : 5000,
        explanation : "그냥 집에 남아서 내놓습니다. 사용한지는 1번이고 그냥 마구잡이로 쓰셔도 됩니다.",
      },
      {
        id : 2,
        name : "신형 텐트",
        category : "텐트",
        image : tent2,
        price : 10000,
        explanation : "신형입니다. 한 번도 사용하지 않았구요. 아껴서 써주세요.",
      },
      {
        id : 3,
        name : "구형 텐트",
        category : "텐트",
        image : tent3,
        price : 12000,
        explanation : "구형입니다. 구형인데 부모님께 물려받은거라 아껴서 사용해주세요.",
      },
      {
        id : 4,
        name : "O형 텐트",
        category : "텐트",
        image : tent4,
        price : 17000,
        explanation : "O형 텐트입니다. 혈액형이 O인 분들이 사용하면 좋더라구요.",
      },
      {
        id : 5,
        name : "무슨 텐트",
        category : "텐트",
        image : tent5,
        price : 14000,
        explanation : "무슨 텐트인지 모르겠습니다. 집구석에 먼지쌓여서 돌아다니길래 내놓습니다.",
      },
      {
        id : 6,
        name : "텐트 텐트",
        category : "텐트",
        image : tent6,
        price : 19000,
        explanation : "텐트 업그레이드 버전인 텐트 텐트입니다. 사용감 미쳤구요 1번 사용했습니다.",
      },

    ])
  })
]