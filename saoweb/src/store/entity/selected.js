class Selected {
  v={}
  hasPoint(si, di) {
    return !!(this.v[si] && this.v[si].has(di))
  }
  addPoint(si, di) {
    if (!this.v[si]) {
      this.v[si] = new Set()
    }
    this.v[si].add(di)
  }
  removePoint(si, di) {
    this.v[si].delete(di)
    if (this.v[si].length === 0) {
      delete this.v[si]
    }
  }
  clickPoint(si, di) {
    if (this.hasPoint(si, di)) {
      this.removePoint(si, di)
      return false
    } else {
      this.addPoint(si, di)
      return true
    }
  }
  getData() {
    return this.v
  }
}
export default Selected
