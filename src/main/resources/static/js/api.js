export default new class {
  constructor(baseUrl) {
    this.baseUrl = baseUrl
  }

  async get(path) {
    return (await axios.get(this.baseUrl + path)).data
  }

  async post(path, data) {
    return (await axios.post(this.baseUrl + path, data)).data
  }

  async put(path, data) {
    return (await axios.put(this.baseUrl + path, data)).data
  }

  async delete(path) {
    return axios.delete(this.baseUrl + path)
  }
}(`http://${window.location.host}`)