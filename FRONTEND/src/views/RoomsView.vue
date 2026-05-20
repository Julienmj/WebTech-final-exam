<template>
  <section>
    <h2 class="text-lg font-semibold text-gray-800 mb-4">Study Rooms</h2>

    <!-- Add Room Form -->
    <form @submit.prevent="submit" aria-label="Add new room"
      class="bg-white border border-gray-200 rounded p-4 mb-6 grid grid-cols-2 gap-3">
      <div class="col-span-2 sm:col-span-1">
        <label for="rname" class="block text-sm text-gray-600 mb-1">Room Name</label>
        <input id="rname" v-model="form.name" required placeholder="e.g. Room A"
          class="w-full border border-gray-300 rounded px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-green-400" />
      </div>
      <div>
        <label for="rcap" class="block text-sm text-gray-600 mb-1">Capacity</label>
        <input id="rcap" v-model.number="form.capacity" type="number" min="1" required
          class="w-full border border-gray-300 rounded px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-green-400" />
      </div>
      <div>
        <label for="rloc" class="block text-sm text-gray-600 mb-1">Location</label>
        <input id="rloc" v-model="form.location" required placeholder="e.g. Block B, Floor 2"
          class="w-full border border-gray-300 rounded px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-green-400" />
      </div>
      <div class="col-span-2 flex items-center gap-3">
        <button type="submit" :disabled="saving"
          class="px-4 py-2 bg-green-600 text-white text-sm rounded hover:bg-green-700 disabled:opacity-50 focus:outline-none focus:ring-2 focus:ring-green-400">
          {{ saving ? 'Adding…' : 'Add Room' }}
        </button>
        <span v-if="message" role="alert"
          :class="isError ? 'text-red-600' : 'text-green-600'"
          class="text-sm">{{ message }}</span>
      </div>
    </form>

    <!-- Room List -->
    <p v-if="loading" class="text-sm text-gray-400">Loading rooms…</p>
    <p v-else-if="store.rooms.length === 0" class="text-sm text-gray-400 text-center py-10">No rooms yet.</p>
    <ul v-else class="grid grid-cols-1 sm:grid-cols-2 gap-4" aria-label="Room list">
      <li v-for="room in store.rooms" :key="room.id"
        class="bg-white border border-gray-200 rounded p-4">
        <div class="flex justify-between items-start">
          <div>
            <p class="font-medium text-gray-800">{{ room.name }}</p>
            <p class="text-sm text-gray-500">{{ room.location }}</p>
            <p class="text-sm text-gray-500">Capacity: {{ room.capacity }}</p>
          </div>
          <span :class="room.availability ? 'bg-green-100 text-green-700' : 'bg-red-100 text-red-700'"
            class="text-xs font-medium px-2 py-1 rounded">
            {{ room.availability ? 'Available' : 'Fully Booked' }}
          </span>
        </div>
      </li>
    </ul>
  </section>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoomStore } from '../stores/roomStore.js'

const store = useRoomStore()
const loading = ref(true)
const saving = ref(false)
const message = ref('')
const isError = ref(false)
const form = ref({ name: '', capacity: 1, location: '' })

onMounted(async () => {
  await store.load()
  loading.value = false
})

async function submit() {
  saving.value = true
  message.value = ''
  try {
    await store.add({ ...form.value, availability: true })
    form.value = { name: '', capacity: 1, location: '' }
    message.value = 'Room added successfully!'
    isError.value = false
  } catch {
    message.value = 'Failed to add room.'
    isError.value = true
  } finally {
    saving.value = false
  }
}
</script>
