import { useEffect, useState } from 'react'
import './App.css'
const API_URL = import.meta.env.VITE_API_URL;

function App() {
  const [applications, setApplications] = useState([])
  const [editingId, setEditingId] = useState(null)
  const [statusFilter, setStatusFilter] = useState('')
  const [companyFilter, setCompanyFilter] = useState('')
  const [sortOrder, setSortOrder] = useState('')

  const [formData, setFormData] = useState({
    company: '',
    position: '',
    status: 'APPLIED',
    dateApplied: '',
    notes: ''
  })


  useEffect(() => {
    const params = new URLSearchParams()

    if (statusFilter) {
      params.append('status', statusFilter)
    }

    if (companyFilter) {
      params.append('company', companyFilter)
    }
    if (sortOrder) {
      params.append('sort', sortOrder)
    }

    const url = `${API_URL}/applications?${params.toString()}`

    fetch(url)
      .then(response => response.json())
      .then(data => {
        setApplications(data)
      })
  }, [statusFilter, companyFilter, sortOrder])

  function handleChange(event) {
    const { name, value } = event.target

    setFormData({
      ...formData,
      [name]: value
    })
  }
  function handleSubmit(event) {
    event.preventDefault()
    const url = editingId
      ? `${API_URL}/applications/${editingId}`
      : `${API_URL}/applications`

    const method = editingId ? 'PUT' : 'POST'

    fetch(url, {
      method: method,
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(formData)
    })
      .then(response => {
        if (!response.ok) {
          throw new Error('Failed to add application')
        }

        return response.json()
      })
      .then(savedApplication => {
        if (editingId) {
          setApplications(
            applications.map(application =>
              application.id === editingId
                ? savedApplication
                : application
            )
          )
        } else {
          setApplications([...applications, savedApplication])
        }

        setFormData({
          company: '',
          position: '',
          status: 'APPLIED',
          dateApplied: '',
          notes: ''
        })

        setEditingId(null)
      })
      .catch(error => {
        console.error(error)
      })
  }
  function handleDelete(id) {
    fetch(`${API_URL}/applications/${id}`, {
      method: 'DELETE'
    })
      .then(response => {
        if (!response.ok) {
          throw new Error('Failed to delete application')
        }

        setApplications(
          applications.filter(application => application.id !== id)
        )
      })
      .catch(error => {
        console.error(error)
      })
  }
  function handleEdit(application) {
    setEditingId(application.id)

    setFormData({
      company: application.company,
      position: application.position,
      status: application.status,
      dateApplied: application.dateApplied,
      notes: application.notes || ''
    })
  }
  console.log(statusFilter)

  return (
    <div className="app">
      <h1 className="page-title">Job Application Tracker</h1>
      <p className="subtitle">Track and manage your job search</p>
      <div className="filter-bar">
        <label>
          <select
            value={statusFilter}
            onChange={event => setStatusFilter(event.target.value)}
          >
            <option value="">All</option>
            <option value="APPLIED">Applied</option>
            <option value="INTERVIEW">Interview</option>
            <option value="OFFER">Offer</option>
            <option value="REJECTED">Rejected</option>
            <option value="WITHDRAWN">Withdrawn</option>
          </select>
        </label>
        <select
          value={sortOrder}
          onChange={event => setSortOrder(event.target.value)}
        >
          <option value="">Default order</option>
          <option value="DESC">Newest first</option>
          <option value="ASC">Oldest first</option>
        </select>
        <input
          type="text"
          placeholder="Search by company"
          value={companyFilter}
          onChange={event => setCompanyFilter(event.target.value)}
        />
      </div>
      <div className="applications-grid">
        {applications.map(application => (
          <div className="application-card" key={application.id}>
            <h2>{application.company}</h2>
            <p>{application.position}</p>
            <p>Status: {' '} <span className={`status-badge ${application.status.toLowerCase()}`}>
  {application.status}
</span></p>
            <p><strong>Applied: </strong>{application.dateApplied}</p>
            <div className = "card-actions">
            <button className="edit-button" onClick={() => handleEdit(application)}>
              Edit
            </button>
            <button className = "delete-button" onClick={() => handleDelete(application.id)}>
              Delete
            </button>
            </div>
          </div>
        ))}
      </div>


      <h2 className="form-title">{editingId ? 'Edit Application' : 'Add Application'}</h2>
      <form onSubmit={handleSubmit} className="application-form">
      <div className="form-grid">
          <div className="form-group">
            <input
              type="text"
              name="company"
              placeholder="Company"
              value={formData.company}
              onChange={handleChange}
            />
          </div>
          <div className="form-group">
            <input
              type="text"
              name="position"
              placeholder="Position"
              value={formData.position}
              onChange={handleChange}
            />
          </div>
          <div className="form-group">
            <select
              name="status"
              value={formData.status}
              onChange={handleChange}
            >
              <option value="APPLIED">Applied</option>
              <option value="INTERVIEW">Interview</option>
              <option value="OFFER">Offer</option>
              <option value="REJECTED">Rejected</option>
              <option value="WITHDRAWN">Withdrawn</option>
            </select>
          </div>
          <div className="form-group">
            <input
              type="date"
              name="dateApplied"
              value={formData.dateApplied}
              onChange={handleChange}
            />
          </div>
          <div className="form-group notes-field">
            <textarea
              name="notes"
              placeholder="Notes"
              value={formData.notes}
              onChange={handleChange}
            />
          </div>
          </div>
          <button className = "submit-button" type="submit">
            {editingId ? 'Save Changes' : 'Add Application'}
          </button>
      </form>
    
    </div >
    
  )
}

export default App
