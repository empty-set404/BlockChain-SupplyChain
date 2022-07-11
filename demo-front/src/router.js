/*
 * @description: 
 * @param: 
 * @return: 
 */
import Home from '@/views/Home';
import Login from '@/views/Login';
import Register from '@/views/Register'
import Receipt from '@/views/Receipt'
import Individual from "@/views/Individual";

export default [
    {
        path: '/',
        redirect: '/home'
    },
    {
        path:'/home',
        component: Home
    },
    {
        path:'/login',
        component: Login
    },
    {
        path: '/register',
        component: Register
    },
    {
        path: '/receipt',
        component: Receipt
    },
    {
        path: '/individual',
        component: Individual
    }
]