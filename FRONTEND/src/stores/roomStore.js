import { defineStore } from 'pinia'
import { ref } from 'vue'
import { fetchRooms, createRoom } from '../services/api.js'

export const useRoomStore = defineStore('rooms', () => {
  const rooms = ref([])

  async function load() {
    rooms.value = await fetchRooms()
  }

  async function add(room) {
    const created = await createRoom(room)
    rooms.value.push(created)
  }

  return { rooms, load, add }
})
