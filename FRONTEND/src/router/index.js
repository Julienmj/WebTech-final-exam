import { createRouter, createWebHistory } from 'vue-router'
import RoomsView from '../views/RoomsView.vue'
import BookingView from '../views/BookingView.vue'
import BookingListView from '../views/BookingListView.vue'

const routes = [
  { path: '/', redirect: '/rooms' },
  { path: '/rooms', component: RoomsView },
  { path: '/booking', component: BookingView },
  { path: '/bookings', component: BookingListView },
]

export default createRouter({
  history: createWebHistory(),
  routes,
})
