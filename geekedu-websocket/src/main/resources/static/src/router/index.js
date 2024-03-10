import VueRouter from 'vue-router'
 
import ChatHome from '../view/pages/chatHome/index.vue'
 
export default new VueRouter({
    routes: [
        {
            path: "/",
            redirect: "/home",
          },
        {
            path: "/home",
            name: "home",
            component: ChatHome,
        },    
    ]
})
