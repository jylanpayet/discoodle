import {createRouter, createWebHashHistory} from 'vue-router'
import Home from "@/views/Home";

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
        component: () => import(/* webpackChunkName: "courses" */ '../views/Courses'),
        children: [
            {
                path: "",
                name: "DefaultCourses",
                component: () =>import(/* webpackChunkName: "courses/subject" */'../components/courses/Group'),
            },
            {
                path: ":id",
                name: "Group",
                component: () =>import(/* webpackChunkName: "courses/subject" */'../components/courses/Group'),
                children: [
                    {
                        path: "",
                        name: "DefaultGroup",
                        redirect: "accueil"
                    }
                ]
            }
        ]
    },
    {
        path: '/messages',
        name: 'Messages',
        component: () => import(/* webpackChunkName: "messages" */ '../views/Messages'),
        children: [
            {
                path: "",
                component: () => import(/* webpackChunkName: "messages/friends" */ '../components/FriendsList')
            },
            {
                path: ':id',
                component: () => import(/* webpackChunkName: "messages/:id" */ '../components/ChatContent'),
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
        component: () => import(/* webpackChunkName: "account" */ '../views/Account'),
        children: [
            {
                path: "infos",
                component: () => import(/* webpackChunkName: "account/infos" */ '../components/settings/AccountInfo')
            },
            {
                path: "settings",
                component: () => import(/* webpackChunkName: "account/settings" */ '../components/settings/AccountInfo')
            },
            {
                path: "home-set",
                component: () => import(/* webpackChunkName: "account/home-set" */ '../components/settings/AccountInfo')
            }
        ]
    }

]

const router = createRouter({
    history: createWebHashHistory(),
    routes
})

export default router
