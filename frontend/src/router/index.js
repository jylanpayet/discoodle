import {createRouter, createWebHashHistory} from 'vue-router'
import Home from "@/views/Home";
import * as path from "path";

const routes = [
    {
        // Default path : Force router to load on the Home component.
        path: '/',
        name: "Default",
        redirect: "/accueil",
        component: Home
    },
    {
        path: '/accueil',
        name: 'Home',
        component: () => import(/* webpackChunkName: "home" */ '../views/Home')
    },
    {
        path: '/cours',
        name: 'Cours',
        component: () => import(/* webpackChunkName: "courses" */ '../views/Courses')
    },
    {
        path: '/messages',
        name: 'Messages',
        component: () => import(/* webpackChunkName: "messages" */ '../views/Messages'),
        children: [
            {
                path: "",
                component: () => import(/* webpackChunkName: "messages/new" */ '../components/AddConversation')
            },
            {
                path: "new",
                component: () => import(/* webpackChunkName: "messages/new" */ '../components/AddConversation')
            },
            {
                path: ':id',
                component: () => import(/* webpackChunkName: "messages/:id" */ '../components/ChatContent'),
                props: () => ({
                    convUUID: path.valueOf()
                })
            },
        ]
    },
    {
        path: '/planning',
        name: 'Planning',
        component: () => import(/* webpackChunkName: "planning" */ '../views/Planning')
    },
    {
        path: '/compte',
        name: 'Compte',
        component: () => import(/* webpackChunkName: "account" */ '../views/Account')
    }

]

const router = createRouter({
    history: createWebHashHistory(),
    routes
})

export default router
