<template>
  <section>
    <h2 class="text-lg font-semibold text-gray-800 mb-4">Book a Study Room</h2>

    <form @submit.prevent="submit" aria-label="Book a room"
      class="bg-white border border-gray-200 rounded p-6 grid grid-cols-1 gap-4 max-w-lg">

      <div>
        <label for="sname" class="block text-sm text-gray-600 mb-1">Student Name</label>
        <input id="sname" v-model="form.studentName" required placeholder="Full name"
          class="w-full border border-gray-300 rounded px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-green-400" />
      </div>

      <div>
        <label for="sid" class="block text-sm text-gray-600 mb-1">Student ID</label>
        <input id="sid" v-model="form.studentId" required placeholder="e.g. STU-001"
          class="w-full border border-gray-300 rounded px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-green-400" />
      </div>

      <div>
        <label for="bdate" class="block text-sm text-gray-600 mb-1">Booking Date</label>
        <input id="bdate" v-model="form.bookingDate" type="date" :min="tomorrow" required
          class="w-full border border-gray-300 rounded px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-green-400" />
      </div>

      <div>
        <label for="room" class="block text-sm text-gray-600 mb-1">Room</label>
        <select id="room" v-model.number="form.roomId" required
          class="w-full border border-gray-300 rounded px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-green-400">
          <option :value="null" disabled>Select a room</option>
          <option v-for="room in roomStore.rooms" :key="room.id" :value="room.id">
            {{ room.name }} — {{ room.location }} (cap. {{ room.capacity }})
          </option>
        </select>
        <p v-if="roomStore.rooms.length === 0 && !loadingRooms" class="text-xs text-red-500 mt-1">No rooms available.</p>
      </div>

      <div>
        <label for="slot" class="block text-sm text-gray-600 mb-1">Time Slot</label>
        <select id="slot" v-model="form.timeSlot" required :disabled="!form.roomId || !form.bookingDate"
          class="w-full border border-gray-300 rounded px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-green-400 disabled:bg-gray-100 disabled:cursor-not-allowed">
          <option value="" disabled>{{ !form.roomId || !form.bookingDate ? 'Select room & date first' : 'Select a time slot' }}</option>
          <option v-for="slot in availableSlots" :key="slot" :value="slot">{{ slot }}</option>
        </select>
        <p v-if="form.roomId && form.bookingDate && availableSlots.length === 0" class="text-xs text-red-500 mt-1">
          All slots are booked for this room on this date.
        </p>
      </div>

      <div>
        <button type="submit" :disabled="saving || !availableSlots.length"
          class="px-4 py-2 bg-green-600 text-white text-sm rounded hover:bg-green-700 disabled:opacity-50 focus:outline-none focus:ring-2 focus:ring-green-400">
          {{ saving ? 'Booking…' : 'Confirm Booking' }}
        </button>
      </div>

      <p v-if="message" role="alert"
        :class="isError ? 'text-red-600' : 'text-green-600'"
        class="text-sm">{{ message }}</p>
    </form>
  </section>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRoomStore } from '../stores/roomStore.js'
import { useBookingStore } from '../stores/bookingStore.js'

const ALL_SLOTS = ['08:00 - 10:00', '10:00 - 12:00', '14:00 - 16:00']

const roomStore = useRoomStore()
const bookingStore = useBookingStore()

const loadingRooms = ref(true)
const saving = ref(false)
const message = ref('')
const isError = ref(false)
const tomorrow = new Date(Date.now() + 86400000).toISOString().split('T')[0]

const form = ref({ studentName: '', studentId: '', roomId: null, bookingDate: '', timeSlot: '' })

// Slots already taken for the selected room+date
const takenSlots = computed(() => {
  if (!form.value.roomId || !form.value.bookingDate) return []
  return bookingStore.bookings
    .filter(b => b.roomId === form.value.roomId && b.bookingDate === form.value.bookingDate)
    .map(b => b.timeSlot)
})

const availableSlots = computed(() => ALL_SLOTS.filter(s => !takenSlots.value.includes(s)))

// Reset timeslot when room or date changes
watch([() => form.value.roomId, () => form.value.bookingDate], () => {
  form.value.timeSlot = ''
})

onMounted(async () => {
  await Promise.all([roomStore.load(), bookingStore.load()])
  loadingRooms.value = false
})

async function submit() {
  if (!form.value.studentName.trim()) return showError('Student name is required.')
  if (!form.value.studentId.trim()) return showError('Student ID is required.')
  if (!form.value.bookingDate) return showError('Please select a date.')
  if (!form.value.roomId) return showError('Please select a room.')
  if (!form.value.timeSlot) return showError('Please select a time slot.')

  saving.value = true
  message.value = ''
  try {
    await bookingStore.add({ ...form.value })
    message.value = 'Room booked successfully!'
    isError.value = false
    form.value = { studentName: '', studentId: '', roomId: null, bookingDate: '', timeSlot: '' }
    await roomStore.load()
  } catch (e) {
    const msg = e.response?.data?.detail || e.response?.data?.message || null
    showError(msg || 'This slot is already booked. Please choose another.')
  } finally {
    saving.value = false
  }
}

function showError(msg) {
  message.value = msg
  isError.value = true
}
</script>
