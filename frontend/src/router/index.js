import {createRouter, createWebHistory} from 'vue-router'
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
        path: '/groupes',
        name: 'Cours',
        component: () => import(/* webpackChunkName: "courses" */ '../views/Courses'),
        children: [
            {
                path: "",
                name: "DefaultCourses",
                component: () =>import(/* webpackChunkName: "courses/subject" */'../components/groups/Group'),
            },
            {
                path: "subject/:id",
                name: "Subjects",
                component: () =>import(/* webpackChunkName: "courses/subject" */'../components/groups/Group'),
                children: [
                    {
                        path: "",
                        name: "Default Subjects",
                        redirect: { name: 'Home Subjects' }
                    },
                    {
                        path: "accueil",
                        name: "Home Subjects",
                        component: () => import('../components/groups/SubjectHome')
                    },
                    {
                        path: "cours",
                        name: "Course Subjects",
                        component: () => import('../components/groups/SubjectCourse'),
                    },
                    {
                        path: "discussion",
                        name: "Server Subjects",
                        component: () => import('../components/groups/SubjectChat'),
                        children: [
                            {
                                path: ":room_id",
                                name: "Channel",
                                component: () => import('../components/groups/Channel')
                            }
                        ]
                    },
                    {
                        path: "notes",
                        name: "Notes Subjects",
                        component: () => import('../components/groups/SubjectNotes')
                    },
                    {
                        path: "parametres",
                        name: "Settings Subjects",
                        component: () => import('../components/groups/SubjectSettings')
                    }
                ]
            },
            {
                path: ":id",
                name: "Group",
                component: () =>import(/* webpackChunkName: "courses/subject" */'../components/groups/Group'),
                children: [
                    {
                        path: "",
                        name: "Default Subjects",
                        redirect: { name: 'Group chat' }
                    },
                    {
                        path: "discussion",
                        name: "Group chat",
                        component: () => import('../components/groups/SubjectChat')
                    },
                    {
                        path: "parametres",
                        name: "Group settings",
                        component: () => import('../components/groups/SubjectSettings')
                    },
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
                name: "Discussion",
                component: () => import(/* webpackChunkName: "messages/:id" */ '../components/ChatContent'),
            },
        ]
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
    history: createWebHistory(),
    routes
})

export default router
