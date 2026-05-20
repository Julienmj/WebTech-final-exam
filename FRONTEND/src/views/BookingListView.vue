<template>
  <section>
    <h2 class="text-lg font-semibold text-gray-800 mb-4">All Bookings</h2>

    <p v-if="loading" class="text-sm text-gray-400">Loading bookings…</p>
    <p v-else-if="store.bookings.length === 0" class="text-sm text-gray-400 text-center py-10">No bookings yet.</p>

    <div v-else class="overflow-x-auto">
      <table class="w-full text-sm border-collapse bg-white border border-gray-200 rounded" aria-label="Bookings table">
        <thead class="bg-gray-100 text-gray-600 text-left">
          <tr>
            <th class="px-4 py-2 font-medium">Student</th>
            <th class="px-4 py-2 font-medium">ID</th>
            <th class="px-4 py-2 font-medium">Room</th>
            <th class="px-4 py-2 font-medium">Date</th>
            <th class="px-4 py-2 font-medium">Slot</th>
            <th class="px-4 py-2 font-medium">Actions</th>
          </tr>
        </thead>
        <tbody>
          <template v-for="b in store.bookings" :key="b.id">
            <!-- Normal row -->
            <tr v-if="editingId !== b.id" class="border-t border-gray-100 hover:bg-gray-50">
              <td class="px-4 py-2">{{ b.studentName }}</td>
              <td class="px-4 py-2">{{ b.studentId }}</td>
              <td class="px-4 py-2">{{ roomName(b.roomId) }}</td>
              <td class="px-4 py-2">{{ b.bookingDate }}</td>
              <td class="px-4 py-2">{{ b.timeSlot }}</td>
              <td class="px-4 py-2 flex gap-2">
                <button @click="startEdit(b)"
                  class="px-3 py-1 text-xs rounded bg-green-600 text-white hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-green-400"
                  :aria-label="`Update booking for ${b.studentName}`">
                  Release
                </button>
                <button @click="cancel(b.id)"
                  class="px-3 py-1 text-xs rounded bg-red-100 text-red-700 hover:bg-red-200 focus:outline-none focus:ring-2 focus:ring-red-400"
                  :aria-label="`Cancel booking for ${b.studentName}`">
                  Cancel
                </button>
              </td>
            </tr>

            <!-- Inline edit row -->
            <tr v-else class="border-t border-green-200 bg-green-50">
              <td class="px-2 py-2">
                <input v-model="editForm.studentName" required
                  class="w-full border border-gray-300 rounded px-2 py-1 text-xs focus:outline-none focus:ring-1 focus:ring-green-400" />
              </td>
              <td class="px-2 py-2">
                <input v-model="editForm.studentId" required
                  class="w-full border border-gray-300 rounded px-2 py-1 text-xs focus:outline-none focus:ring-1 focus:ring-green-400" />
              </td>
              <td class="px-2 py-2">
                <select v-model.number="editForm.roomId"
                  class="w-full border border-gray-300 rounded px-2 py-1 text-xs focus:outline-none focus:ring-1 focus:ring-green-400">
                  <option v-for="room in roomStore.rooms" :key="room.id" :value="room.id">{{ room.name }}</option>
                </select>
              </td>
              <td class="px-2 py-2">
                <input v-model="editForm.bookingDate" type="date" required
                  class="w-full border border-gray-300 rounded px-2 py-1 text-xs focus:outline-none focus:ring-1 focus:ring-green-400" />
              </td>
              <td class="px-2 py-2">
                <select v-model="editForm.timeSlot"
                  class="w-full border border-gray-300 rounded px-2 py-1 text-xs focus:outline-none focus:ring-1 focus:ring-green-400">
                  <option v-for="slot in editAvailableSlots" :key="slot" :value="slot">{{ slot }}</option>
                </select>
              </td>
              <td class="px-2 py-2 flex gap-2">
                <button @click="saveEdit(b.id)"
                  class="px-3 py-1 text-xs rounded bg-green-600 text-white hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-green-400">
                  Save
                </button>
                <button @click="editingId = null"
                  class="px-3 py-1 text-xs rounded bg-gray-200 text-gray-700 hover:bg-gray-300 focus:outline-none focus:ring-2 focus:ring-gray-400">
                  Discard
                </button>
              </td>
            </tr>
            <tr v-if="editingId === b.id && editError" class="bg-green-50">
              <td colspan="6" class="px-4 pb-2 text-xs text-red-600" role="alert">{{ editError }}</td>
            </tr>
          </template>
        </tbody>
      </table>
    </div>
  </section>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useBookingStore } from '../stores/bookingStore.js'
import { useRoomStore } from '../stores/roomStore.js'

const ALL_SLOTS = ['08:00 - 10:00', '10:00 - 12:00', '14:00 - 16:00']

const store = useBookingStore()
const roomStore = useRoomStore()
const loading = ref(true)
const editingId = ref(null)
const editError = ref('')
const editForm = ref({})

onMounted(async () => {
  await Promise.all([store.load(), roomStore.load()])
  loading.value = false
})

function roomName(roomId) {
  const room = roomStore.rooms.find(r => r.id === roomId)
  return room ? room.name : `Room #${roomId}`
}

function startEdit(b) {
  editingId.value = b.id
  editError.value = ''
  editForm.value = { ...b }
}

// Slots taken for the edited booking's room+date, excluding the booking being edited
const editAvailableSlots = computed(() => {
  if (!editForm.value.roomId || !editForm.value.bookingDate) return ALL_SLOTS
  const taken = store.bookings
    .filter(b => b.id !== editingId.value
      && b.roomId === editForm.value.roomId
      && b.bookingDate === editForm.value.bookingDate)
    .map(b => b.timeSlot)
  return ALL_SLOTS.filter(s => !taken.includes(s))
})

async function saveEdit(id) {
  editError.value = ''
  try {
    await store.update(id, { ...editForm.value })
    editingId.value = null
    await roomStore.load()
  } catch (e) {
    const msg = e.response?.data?.detail || e.response?.data?.message || null
    editError.value = msg || 'This slot is already booked. Please choose another.'
  }
}

async function cancel(id) {
  await store.remove(id)
  await roomStore.load()
}
</script>
