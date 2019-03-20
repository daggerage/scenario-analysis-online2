import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/views/layout/Layout'

/** note: Submenu only appear when children.length>=1
 *  detail see  https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 **/

/**
* hidden: true                   if `hidden:true` will not show in the sidebar(default is false)
* alwaysShow: true               if set true, will always show the root menu, whatever its child routes length
*                                if not set alwaysShow, only more than one route under the children
*                                it will becomes nested mode, otherwise not show the root menu
* redirect: noredirect           if `redirect:noredirect` will no redirect in the breadcrumb
* name:'router-name'             the name is used by <keep-alive> (must set!!!)
* meta : {
    roles: ['admin','editor']    will control the page roles (you can set multiple roles)
    title: 'title'               the name show in submenu and breadcrumb (recommend set)
    icon: 'svg-name'             the icon show in the sidebar
    noCache: true                if true, the page will no be cached(default is false)
    breadcrumb: false            if false, the item will hidden in breadcrumb(default is true)
  }
**/
export const constantRouterMap = [
  {
    path: '/redirect',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '/redirect/:path*',
        component: () => import('@/views/redirect/index')
      }
    ]
  },
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },
  {
    path: '/auth-redirect',
    component: () => import('@/views/login/authredirect'),
    hidden: true
  },
  {
    path: '/404',
    component: () => import('@/views/errorPage/404'),
    hidden: true
  },
  {
    path: '/401',
    component: () => import('@/views/errorPage/401'),
    hidden: true
  },
  {
    path: '',
    component: Layout,
    redirect: 'homepage',
    children: [
      {
        path: 'homepage',
        component: () => import('@/views/homepage/index'),
        name: 'Homepage',
        meta: { title: '主页', icon: 'home-fill', noCache: true }
      }
    ]
  },
  {
    path: '/map-overview',
    component: Layout,
    redirect: '/map-overview/index',
    children: [
      {
        path: 'index',
        component: () => import('@/views/map-overview/index'),
        name: 'MapOverview',
        meta: { title: '地图总览', icon: 'location-fill', noCache: true }
      }
    ]
  },
  {
    path: '/bmp-display',
    component: Layout,
    redirect: '/bmp-display/index',
    children: [
      {
        path: 'index',
        component: () => import('@/views/bmp-display/index'),
        name: 'BMPDisplay',
        meta: { title: 'BMP展示', icon: 'detail-fill', noCache: true }
      }
    ]
  },
  {
    path: '/scenario-config',
    component: Layout,
    redirect: '/scenario-config/index',
    children: [
      {
        path: 'index',
        component: () => import('@/views/scenario-config/index'),
        name: 'ScenarioConfig',
        meta: { title: '情景配置', icon: 'wrench-fill', noCache: true }
      }
    ]
  },
  {
    path: '/scenario-history',
    component: Layout,
    redirect: '/scenario-history/index',
    children: [
      {
        path: 'index',
        component: () => import('@/views/scenario-history/index'),
        name: 'ScenarioHistory',
        meta: { title: '情景分析历史', icon: 'folder-fill', noCache: true }
      },
      {
        path: 'detail',
        component: () => import('@/views/scenario-history/detail'),
        name: 'HistoryDetail',
        meta: { title: '情景详情对比', icon: 'folder-fill', noCache: true },
        hidden: true
      }

    ]
  }

]

export default new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRouterMap
})

export const asyncRouterMap = [
  // {
  //   path: '/permission',
  //   component: Layout,
  //   redirect: '/permission/index',
  //   alwaysShow: true, // will always show the root menu
  //   meta: {
  //     title: 'permission',
  //     icon: 'lock',
  //     roles: ['admin', 'editor'] // you can set roles in root nav
  //   },
  //   children: [
  //     {
  //       path: 'page',
  //       component: () => import('@/views/permission/page'),
  //       name: 'PagePermission',
  //       meta: {
  //         title: 'pagePermission',
  //         roles: ['admin'] // or you can only set roles in sub nav
  //       }
  //     },
  //     {
  //       path: 'directive',
  //       component: () => import('@/views/permission/directive'),
  //       name: 'DirectivePermission',
  //       meta: {
  //         title: 'directivePermission'
  //         // if do not set roles, means: this page does not require permission
  //       }
  //     }
  //   ]
  // },
  // {
  //   path: '/example',
  //   component: Layout,
  //   redirect: '/example/list',
  //   name: 'Example',
  //   meta: {
  //     title: 'example',
  //     icon: 'example'
  //   },
  //   children: [
  //     {
  //       path: 'create',
  //       component: () => import('@/views/example/create'),
  //       name: 'CreateArticle',
  //       meta: { title: 'createArticle', icon: 'edit' }
  //     },
  //     {
  //       path: 'edit/:id(\\d+)',
  //       component: () => import('@/views/example/edit'),
  //       name: 'EditArticle',
  //       meta: { title: 'editArticle', noCache: true },
  //       hidden: true
  //     },
  //     {
  //       path: 'list',
  //       component: () => import('@/views/example/list'),
  //       name: 'ArticleList',
  //       meta: { title: 'articleList', icon: 'list' }
  //     }
  //   ]
  // },
  { path: '*', redirect: '/404', hidden: true }
]
